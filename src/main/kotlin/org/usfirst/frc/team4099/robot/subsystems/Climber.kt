package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.Talon
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop

class Climber private constructor() : Subsystem {
    private val pneumaticPiston_F1: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_F1_FORWARD_ID, Constants.Climber.CLIMBER_F1_REVERSE_ID)
    private val pneumaticPiston_F2: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_F2_FORWARD_ID, Constants.Climber.CLIMBER_F2_REVERSE_ID)
    private val pneumaticPiston_B1: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_B1_FORWARD_ID, Constants.Climber.CLIMBER_B1_REVERSE_ID)
    private val pneumaticPiston_B2: DoubleSolenoid = DoubleSolenoid(Constants.Climber.CLIMBER_B2_FORWARD_ID, Constants.Climber.CLIMBER_B2_REVERSE_ID)

    enum class ClimberState {
        CLIMBING, NOT_CLIMBING, UNCLIMBING
    }
    var climberState = ClimberState.NOT_CLIMBING
    override fun outputToSmartDashboard() {
    }

    override fun stop() {

    }

    override fun zeroSensors() {
    }

    private fun frontPistonsDown() {
        pneumaticPiston_F1.set(pneumaticShifter_F1.Value.kForward)
        pneumaticPiston_F2.set(pneumaticShifter_F2.Value.kForward)
    }
    private fun backPistonsDown() {
        pneumaticPiston_B1.set(pneumaticShifter_B1.Value.kForward)
        pneumaticPiston_B2.set(pneumaticShifter_B2.Value.kForward)
    }
    private fun frontPistonsUp() {
        pneumaticPiston_F1.set(pneumaticShifter_F1.Value.kReverse)
        pneumaticPiston_F2.set(pneumaticShifter_F2.Value.kReverse)
    }
    private fun backPistonsUp() {
        pneumaticPiston_F1.set(pneumaticShifter_B1.Value.kReverse)
        pneumaticPiston_F1.set(pneumaticShifter_B2.Value.kReverse)
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            frontPistonsUp()
            backPistonsUp()
        }
        override fun onLoop() {


        }
        override fun onStop() {
            frontPistonsUp()
            backPistonsUp()
        }
    }

}