package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import com.revrobotics.CANEncoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop
import edu.wpi.first.wpilibj.DoubleSolenoid

class Climber private constructor() : Subsystem {
    private val climbMotor: CANSparkMax = CANSparkMax(Constants.Climber.CLIMBER_SPARK_ID, MotorType.kBrushless)
    private val driveMotor: CANSparkMax = CANSparkMax(Constants.Climber.DRIVE_SPARK_ID, MotorType.kBrushless)
    private val climbEncoder: CANEncoder = climbMotor.encoder
    private val driveEncoder: CANEncoder = driveMotor.encoder
    private val climbPIDController :CANPIDController = climbMotor.pidController
    private val pneumaticShifter: DoubleSolenoid = DoubleSolenoid(Constants.Climber.SHIFTER_FORWARD_ID, Constants.Climber.SHIFTER_REVERSE_ID)

    init{
        climbPIDController.setP(Constants.Climber.CLIMBER_KP)
        climbPIDController.setI(Constants.Climber.CLIMBER_KI)
        climbPIDController.setD(Constants.Climber.CLIMBER_KD)
        climbPIDController.setIZone(Constants.Climber.CLIMBER_KIz)
        climbPIDController.setFF(Constants.Climber.CLIMBER_KF)
        climbPIDController.setOutputRange(-Constants.Climber.MAX_OUTPUT, Constants.Climber.MAX_OUTPUT)
    }

    enum class ClimberState (val target : Double) {
        STILL (Double.NaN), OPENLOOP (Double.NaN), DOWN (-100.0), UP (0.0), FORWARD (Double.NaN)
    }
    var climberState = ClimberState.STILL

    override fun outputToSmartDashboard() {
        SmartDashboard.putString("climber/climberState", climberState.toString())
    }

    override fun stop() {
    }

    override fun zeroSensors() {
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            climberState = ClimberState.STILL

        }
        override fun onLoop() {
            println("Climber Position: " + climbEncoder.position)
            synchronized(this@Climber) {
                when(climberState) {
                    ClimberState.STILL -> {
                        climbMotor.set(0.0)
                    }
                    ClimberState.OPENLOOP -> {
                        return
                    }
                    ClimberState.DOWN -> {      //climbing
                        climberDown()
                    }
                    ClimberState.UP -> {      //climbing
                        climberUp()
                    }
                    ClimberState.FORWARD -> {   //climbed
                        drive(1.0)
                    }

                }
            }


        }
        override fun onStop() {
            climberState = ClimberState.STILL

        }
    }
    companion object {
        val instance = Climber()
    }

    fun setOpenLoop(power : Double){
        climberState = ClimberState.OPENLOOP
        climbMotor.set(power)
    }

    fun setLatch(wantsOut : Boolean){
        if (wantsOut){

        }
    }

    fun drive(speed : Double){
        climberDown()
        driveMotor.set(speed)
    }

    fun climberDown(){
        pneumaticShifter.set(DoubleSolenoid.Value.kForward)
        climbPIDController.setReference(climberState.target, ControlType.kPosition)
    }

    fun climberUp(){
        pneumaticShifter.set(DoubleSolenoid.Value.kReverse)
        climbPIDController.setReference(climberState.target, ControlType.kPosition)
    }
}