package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import com.revrobotics.CANEncoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop
import edu.wpi.first.wpilibj.DoubleSolenoid

class Climber private constructor() : Subsystem {
    private val climbMotor: CANSparkMax = CANSparkMax(Constants.Climber.CLIMBER_SPARK_ID, MotorType.kBrushless)
    private val driveMotor: TalonSRX = CANMotorControllerFactory.createDefaultTalon(Constants.Climber.DRIVE_TALON_ID) //final
    private val climbEncoder: CANEncoder = climbMotor.encoder
    private val climbPIDController: CANPIDController = climbMotor.pidController
    private val neighbor: DoubleSolenoid = DoubleSolenoid(Constants.Climber.FEET_SOLENOID_FORWARD,Constants.Climber.FEET_SOLENOID_REVERSE)
    private var tare: Double = 0.0
    var movementState = MovementState.STILL
        private set
    var observedElevatorPosition = 0.0
        private set
    var observedClimberVelocity = 0.0
    private set

    var climberState = ClimberState.OPEN_LOOP
    var feetState = false
        set(wantsFeet) {
            //neighbor.set(if (wantsFeet) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
            field = wantsFeet
        }

    enum class FeetState {
        EXTEND, RETRACT
    }

    var feet = FeetState.RETRACT



    var lastClimbPosition = 0.0

    var brakeMode: CANSparkMax.IdleMode = CANSparkMax.IdleMode.kCoast //sets whether the brake mode should be coast (no resistance) or by force
        set(type) {
            if (brakeMode != type) {
                climbMotor.idleMode = type
            }
            field = type
        }

    private fun setClimberPosition(position: ClimberState) {
        var target = position.targetPos + tare
    }

    private fun setClimberPosition(targetPos : Double) {
        var target = targetPos
        if (target == Double.NaN) {
            target = observedElevatorPosition
        } else {
            //observedElevatorPosition = target
        }
        climbPIDController.setReference(target + tare, ControlType.kSmartMotion)


    }

    fun setOpenLoop(power: Double) {
        climberState = ClimberState.OPEN_LOOP
//        println("Elevator: " + observedElevatorPosition)
        if(observedElevatorPosition > Constants.Climber.CLIMBER_SOFT_LIMIT + tare && power < 0.0){ //CHANGE SOFT LIMIT
            climbPIDController.setReference(power, ControlType.kVoltage)
        }
        else {
            climbPIDController.setReference(power, ControlType.kVoltage)
        }
    }

    fun setClimberVelocity(inchesPerSecond: Double) {
        //remove, just for testing
        if (inchesPerSecond == 0.0){
            setClimberPosition(lastClimbPosition)
        }
        else {
            lastClimbPosition = observedElevatorPosition
            climbMotor.set(inchesPerSecond)
        }
    }

    fun setOpenDrive(power: Double){
        driveMotor.set(ControlMode.PercentOutput, power)
    }
    init{
        climbPIDController.setP(Constants.Climber.CLIMBER_KP)
        climbPIDController.setI(Constants.Climber.CLIMBER_KI)
        climbPIDController.setD(Constants.Climber.CLIMBER_KD)
        climbPIDController.setIZone(Constants.Climber.CLIMBER_KIz)
        climbPIDController.setFF(Constants.Climber.CLIMBER_KF)
        climbPIDController.setOutputRange(-Constants.Climber.MAX_OUTPUT, Constants.Climber.MAX_OUTPUT)

        climbPIDController.setSmartMotionMaxVelocity(30.0, 0)
        climbPIDController.setSmartMotionMinOutputVelocity(0.0, 0)
        climbPIDController.setSmartMotionMaxAccel(100.0, 0)
        climbPIDController.setSmartMotionAllowedClosedLoopError(2.5, 0)

        brakeMode = CANSparkMax.IdleMode.kCoast
    }

    enum class ClimberState (val targetPos: Double){
        LEVEL_THREE(Constants.Climber.LEVEL_THREE_POSITION),
        LEVEL_TWO(Constants.Climber.LEVEL_TWO),
        LEVEL_TWO_HALF(Constants.Climber.LEVEL_TWO_HALF),
        STOW(0.0),
        VELOCITY_CONTROL(Double.NaN),
        OPEN_LOOP(Double.NaN)
    }

    enum class MovementState {
        UP, DOWN, STILL
    }


    override fun outputToSmartDashboard() {
        SmartDashboard.putString("climber/climberState", climberState.toString())
        SmartDashboard.putNumber("climber/encoderValue", climbEncoder.position)
        SmartDashboard.putNumber("climber/encoderVelocity", climbEncoder.velocity)
    }

    override fun stop() {
    }

    override fun zeroSensors() {
        tare = climbEncoder.position
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            climberState = ClimberState.OPEN_LOOP
            feetState = false
            zeroSensors()
        }
        override fun onLoop() {
            println("Climber Position: " + (climbEncoder.position - tare))
            observedElevatorPosition = climbEncoder.position
            observedClimberVelocity = climbEncoder.velocity
            synchronized(this@Climber) {
                when(climberState) {
                    ClimberState.VELOCITY_CONTROL -> {
                        return
                    }
                    ClimberState.OPEN_LOOP -> {
                        return
                    }
                    else -> {
                        setClimberPosition(climberState.targetPos)
                    }
                }
                when {
                    observedClimberVelocity in -1 .. 1 -> movementState = MovementState.STILL
                    observedClimberVelocity > 1 -> movementState = MovementState.UP
                    observedClimberVelocity < 1 -> movementState = MovementState.DOWN
                }
                when(feet){
                    FeetState.RETRACT -> {
                        neighbor.set(DoubleSolenoid.Value.kForward)
                    }
                    FeetState.EXTEND -> {
                        neighbor.set(DoubleSolenoid.Value.kReverse)
                    }
                }
            }


        }
        override fun onStop() {
            climberState = ClimberState.STOW
        }
    }
    companion object {
        val instance = Climber()
    }
}