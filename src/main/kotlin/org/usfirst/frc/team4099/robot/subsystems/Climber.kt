package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.Talon
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop

class Climber private constructor() : Subsystem {
    private val pneumaticPiston_F1: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_F1_FORWARD_ID, Constants.Climber.CLIMBER_F1_REVERSE_ID)
    private val pneumaticPiston_B1: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_B1_FORWARD_ID, Constants.Climber.CLIMBER_B1_REVERSE_ID)

    enum class ClimberState (val toString : String){
        FRONT_DOWN("Front Down"), BACK_DOWN("Back Down"), BOTH_UP ("Both Up")
    }
    var climberState = ClimberState.BOTH_UP
    override fun outputToSmartDashboard() {
        SmartDashboard.putString("climber/climberState", climberState.toString())
    }

    override fun stop() {

    }

    override fun zeroSensors() {
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            climberState = ClimberState.BOTH_UP

        }
        override fun onLoop() {
            synchronized(this@Climber) {
                when(climberState) {
                    ClimberState.FRONT_DOWN -> {
                        pneumaticPiston_F1.set(DoubleSolenoid.Value.kForward)
                    }
                    ClimberState.BACK_DOWN -> {
                        pneumaticPiston_B1.set(DoubleSolenoid.Value.kForward)
                    }
                    ClimberState.BOTH_UP -> {
                        pneumaticPiston_F1.set(DoubleSolenoid.Value.kReverse)
                        pneumaticPiston_B1.set(DoubleSolenoid.Value.kReverse)
                    }

                }
            }


        }
        override fun onStop() {
            climberState = ClimberState.BOTH_UP

        }
    }
    companion object {
        val instance = Climber()

    }
}