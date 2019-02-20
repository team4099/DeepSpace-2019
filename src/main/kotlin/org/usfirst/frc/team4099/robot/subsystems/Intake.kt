package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.Talon
import edu.wpi.first.wpilibj.Timer
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop
import java.sql.Time
import kotlin.system.measureTimeMillis


/**
 * @author Team 4099
 *
 * This class is the constructor for the Intake subsystem
 *
 * @constructor Creates the Intake subsystem
 *
 */
class Intake private constructor() : Subsystem {

    private val talon = TalonSRX(Constants.Intake.INTAKE_TALON_ID)
    private val extender: DoubleSolenoid = DoubleSolenoid(Constants.Intake.EXTENDER_FORWARD_ID,
            Constants.Intake.EXTENDER_REVERSE_ID)
    private val deployer: DoubleSolenoid = DoubleSolenoid(Constants.Intake.DEPLOYER_FORWARD_ID,
            Constants.Intake.DEPLOYER_REVERSE_ID)

    var intakeState = IntakeState.IN
    var hatchState = HatchState.IN
    private var hatchOutStart = 0.0
    private var intakePower = 0.0
//    var hatchOut = false
//        set (wantsOut) {
//            deployer.set(if (wantsOut) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
//            field = wantsOut
//        }
//
//    var extended = false
//        set (wantsExtended) {
//            if(wantsExtended){
//                hatchOutStart = Timer.getFPGATimestamp()
//            }
//            //extender.set(if (wantsExtended) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
//            field = wantsExtended
//        }


    enum class IntakeState {
        IN, STOP, SLOW_OUT, FAST_OUT, SLOW
    }

    enum class HatchState {
        OUT, IN
    }

    override fun outputToSmartDashboard() {
        //SmartDashboard.putNumber("intake/intakePower", intakePower)
        //SmartDashboard.putBoolean("intake/isUp", up)
        //SmartDashboard.putNumber("intake/current", BrownoutDefender.instance.getCurrent(7))
    }

    init{
        talon.configPeakCurrentLimit(10)
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
        talon.set(ControlMode.PercentOutput,power)
    }

    /**
     * Handles power to intake during each loop cycle
     * @constructor Creates loop that controls power to intake during each loop cycle
     */
    val loop: Loop = object : Loop {
        override fun onStart() {
            hatchState = HatchState.IN
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
                when (hatchState){
                    HatchState.IN -> {
                        extender.set(DoubleSolenoid.Value.kReverse)
                        //deployer.set(DoubleSolenoid.Value.kReverse)
                    }
                    HatchState.OUT -> {
                        extender.set(DoubleSolenoid.Value.kForward)
                        //deployer.set(DoubleSolenoid.Value.kForward)
                    }
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