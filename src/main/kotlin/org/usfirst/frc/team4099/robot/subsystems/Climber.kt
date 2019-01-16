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
    private val pneumaticPiston_F2: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_F2_FORWARD_ID, Constants.Climber.CLIMBER_F2_REVERSE_ID)
    private val pneumaticPiston_B1: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_B1_FORWARD_ID, Constants.Climber.CLIMBER_B1_REVERSE_ID)
    private val pneumaticPiston_B2: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_B2_FORWARD_ID, Constants.Climber.CLIMBER_B2_REVERSE_ID)

    enum class ClimberState {
        FRONT_DOWN, BACK_DOWN, BOTH_UP
    }
    var climberState = ClimberState.BOTH_UP
    override fun outputToSmartDashboard() {
        SmartDashboard.putString("climber/climberState", climberState.toString())
    }

    override fun stop() {

    }

    override fun zeroSensors() {
    }

    private fun frontPistonsDown() {
        pneumaticPiston_F1.set(DoubleSolenoid.Value.kForward)
        pneumaticPiston_F2.set(DoubleSolenoid.Value.kForward)
        climberState = ClimberState.FRONT_DOWN
    }
    private fun backPistonsDown() {
        pneumaticPiston_B1.set(DoubleSolenoid.Value.kForward)
        pneumaticPiston_B2.set(DoubleSolenoid.Value.kForward)
        climberState = ClimberState.BACK_DOWN
    }
    private fun frontPistonsUp() {
        if (climberState == ClimberState.FRONT_DOWN) {
            pneumaticPiston_F1.set(DoubleSolenoid.Value.kReverse)
            pneumaticPiston_F2.set(DoubleSolenoid.Value.kReverse)
            climberState = ClimberState.BOTH_UP
        }
    }
    private fun backPistonsUp() {
        if (climberState == ClimberState.BACK_DOWN) {
            pneumaticPiston_F1.set(DoubleSolenoid.Value.kReverse)
            pneumaticPiston_F1.set(DoubleSolenoid.Value.kReverse)
            climberState = ClimberState.BOTH_UP
        }


    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            frontPistonsUp()
            backPistonsUp()

        }
        override fun onLoop() {
            synchronized(this@Climber) {
                when(climberState) {
                    ClimberState.FRONT_DOWN -> {
                        pneumaticPiston_F1.set(DoubleSolenoid.Value.kForward)
                        pneumaticPiston_F2.set(DoubleSolenoid.Value.kForward)
                    }
                    ClimberState.BACK_DOWN -> {
                        pneumaticPiston_B1.set(DoubleSolenoid.Value.kForward)
                        pneumaticPiston_B2.set(DoubleSolenoid.Value.kForward)
                    }
                    ClimberState.BOTH_UP -> {
                        pneumaticPiston_F1.set(DoubleSolenoid.Value.kReverse)
                        pneumaticPiston_F2.set(DoubleSolenoid.Value.kReverse)
                        pneumaticPiston_B1.set(DoubleSolenoid.Value.kReverse)
                        pneumaticPiston_B2.set(DoubleSolenoid.Value.kReverse)

                    }

                }
            }


        }
        override fun onStop() {
            frontPistonsUp()
            backPistonsUp()

        }
    }
    companion object {
        val instance = Climber()

    }
}