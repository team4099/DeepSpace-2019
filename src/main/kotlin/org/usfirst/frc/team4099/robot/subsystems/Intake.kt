package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.Talon
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Loop


/**
 * @author Team 4099
 *
 * This class is the constructor for the Intake subsystem
 *
 * @constructor Creates the Intake subsystem
 *
 */
class Intake private constructor() : Subsystem {

    private val talon = Talon(Constants.Intake.INTAKE_TALON_ID)
    private val pneumaticShifter: DoubleSolenoid = DoubleSolenoid(Constants.Intake.SHIFTER_FORWARD_ID,
            Constants.Intake.SHIFTER_REVERSE_ID)

    var intakeState = IntakeState.IN
    private var intakePower = 0.0
    var pistonsOut = false
        set (wantsUp) {
            pneumaticShifter.set(if (wantsUp) DoubleSolenoid.Value.kReverse else DoubleSolenoid.Value.kForward)
            field = wantsUp
        }


    enum class IntakeState {
        IN, STOP, SLOW_OUT, FAST_OUT, SLOW
    }

    override fun outputToSmartDashboard() {
        //SmartDashboard.putNumber("intake/intakePower", intakePower)
        //SmartDashboard.putBoolean("intake/isUp", up)
        //SmartDashboard.putNumber("intake/current", BrownoutDefender.instance.getCurrent(7))
    }

    /**
     * stops intake
     */
    @Synchronized override fun stop() {
        intakeState = IntakeState.STOP
        setIntakePower(0.0)
    }

    /**
     * sets rightTalon to positive power and Talon to negative power
     * @param power a double that is the power for the intake
     */
    private fun setIntakePower(power: Double) {
        talon.set(power)
    }

    /**
     * Handles power to intake during each loop cycle
     * @constructor Creates loop that controls power to intake during each loop cycle
     */
    val loop: Loop = object : Loop {
        override fun onStart() {
            pistonsOut = false
            intakeState = IntakeState.STOP
        }

        /**
         * Sets Intake to -1 if pulling in, to 0 if stationary, and 1 if pushing out
         */
        override fun onLoop() {
            synchronized(this@Intake) {
                when (intakeState) {
                    IntakeState.IN -> setIntakePower(-1.0)
                    IntakeState.STOP -> setIntakePower(0.0)
                    IntakeState.SLOW_OUT -> setIntakePower(0.5)
                    IntakeState.FAST_OUT -> setIntakePower(1.0)
                    IntakeState.SLOW -> setIntakePower(-0.5)
                }
            }
        }

        override fun onStop() = stop()

    }

    companion object {
        val instance = Intake()
    }

    override fun zeroSensors() { }
}