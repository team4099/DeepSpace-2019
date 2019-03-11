package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import com.revrobotics.CANEncoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop


class Climber private constructor() : Subsystem {
    private val climbMotor: CANSparkMax = CANSparkMax(Constants.Climber.CLIMBER_SPARK_ID, MotorType.kBrushless)
    private val driveMotor: TalonSRX = CANMotorControllerFactory.createDefaultTalon(Constants.Climber.DRIVE_TALON_ID)
    private val climbEncoder: CANEncoder = climbMotor.encoder
    private val climbPIDController: CANPIDController = climbMotor.pidController
    var movementState = MovementState.STILL
        private set
    var observedElevatorPosition = 0.0
        private set
    var observedClimberVelocity = 0.0
    private set

    private fun setClimberPosition(position: ClimberState) {
        var target = position.targetPos
        if (target == Double.NaN) {
            target = observedElevatorPosition
        } else {
            //observedElevatorPosition = target
        }
    }

    fun setOpenLoop(power: Double) {
        climberState = ClimberState.OPEN_LOOP
//        println("Elevator: " + observedElevatorPosition)
        if(observedElevatorPosition > Constants.Climber.CLIMBER_SOFT_LIMIT  && power < 0.0){ //CHANGE SOFT LIMIT
            climbMotor.setReference(power, ControlType.kVoltage)
        }
        else {
            climbMotor.setReference(power, ControlType.kVoltage)
        }
    }

    fun setClimberVelocity(inchesPerSecond: Double) {
        climbMotor.setReference(inchesPerSecond, ControlType.kVelocity)

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
    }

    enum class ClimberState (val targetPos: Double){
        LEVEL_THREE(0.0), LEVEL_TWO(0.0), LEVEL_TWO_HALF(0.0), STOW(0.0),
        VELOCITY_CONTROL(Double.NaN), OPEN_LOOP(Double.NaN)
    }

    enum class MovementState {
        UP, DOWN, STILL
    }

    var climberState = ClimberState.STOW

    override fun outputToSmartDashboard() {
        SmartDashboard.putString("climber/climberState", climberState.toString())
    }

    override fun stop() {

    }

    override fun zeroSensors() {
        climbEncoder.setPosition(0.0)
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            climberState = ClimberState.STOW

        }
        override fun onLoop() {
            synchronized(this@Climber) {
                when(climberState) {
                    ClimberState.LEVEL_THREE -> {
                        setClimberPosition(ClimberState.LEVEL_THREE)
                    }
                    ClimberState.LEVEL_TWO -> {
                        setClimberPosition(ClimberState.LEVEL_TWO)
                    }
                    ClimberState.LEVEL_TWO_HALF -> {
                        setClimberPosition(ClimberState.LEVEL_TWO_HALF)
                    }
                    ClimberState.STOW -> {
                        setClimberPosition(ClimberState.STOW)
                    }
                    ClimberState.VELOCITY_CONTROL -> {
                        return
                    }
                    ClimberState.OPEN_LOOP -> {
                        return
                    }
                }
                when {
                    observedClimberVelocity in -1 .. 1 -> movementState = MovementState.STILL
                    observedClimberVelocity > 1 -> movementState = MovementState.UP
                    observedClimberVelocity < 1 -> movementState = MovementState.DOWN
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