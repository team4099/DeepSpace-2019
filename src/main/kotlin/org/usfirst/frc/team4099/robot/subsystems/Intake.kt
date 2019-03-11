package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj.DoubleSolenoid
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard


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
    var hatchState = HatchState.CLOSED
    var deployState = DeployState.IN
    private var intakePower = 0.0
    public var isHatchOpen = false

    enum class IntakeState {
        IN, STOP, HOLDING, OUT, SLOW
    }

    enum class HatchState {
        OPEN, CLOSED
    }
    enum class DeployState {
        IN, OUT
    }

    override fun outputToSmartDashboard() {
//        SmartDashboard.putString("intake/hatchState", hatchState.toString())
//        SmartDashboard.putString("intake/intakeState", intakeState.toString())
        //SmartDashboard.putNumber("intake/intakePower", intakePower)
        //SmartDashb
        // oard.putBoolean("intake/isUp", up)
        //SmartDashboard.putNumber("intake/current", BrownoutDefender.instance.getCurrent(7))
        SmartDashboard.putBoolean("intake/hatchOpen", isHatchOpen)
    }

    init{
        talon.configPeakCurrentLimit(10)
        talon.inverted = true
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
    fun setIntakePower(power: Double) {
        talon.set(ControlMode.PercentOutput,power)
    }

    /**
     * Handles power to intake during each loop cycle
     * @constructor Creates loop that controls power to intake during each loop cycle
     */
    val loop: Loop = object : Loop {
        override fun onStart() {
            hatchState = HatchState.CLOSED
            intakeState = IntakeState.STOP
        }

        /**
         * Sets Intake to -1 if pulling in, to 0 if stationary, and 1 if pushing out
         */
        override fun onLoop() {
            synchronized(this@Intake) {
                when (intakeState) {
                    IntakeState.IN -> setIntakePower(-1.0)
                    IntakeState.HOLDING -> setIntakePower(-0.2)
                    IntakeState.STOP -> setIntakePower(0.0)
                    IntakeState.OUT -> setIntakePower(1.0)
                }
                when (hatchState){
                    HatchState.CLOSED -> {
                        extender.set(DoubleSolenoid.Value.kReverse)
                        isHatchOpen = false
                    }
                    HatchState.OPEN -> {
                        extender.set(DoubleSolenoid.Value.kForward)
                        isHatchOpen = true
                    }
                }
                when (deployState){
                    DeployState.IN -> {
                        deployer.set(DoubleSolenoid.Value.kReverse)
                    }
                    DeployState.OUT -> {
                        deployer.set(DoubleSolenoid.Value.kForward)
                    }
                }
            }
//            extended = false
//            println(extended)
//            if(hatchOut && Timer.getFPGATimestamp() - hatchOutStart > 0.2){
//                extended = false
//                //hatchOut = false
//            }
//            hatchOut = false
        }

        override fun onStop() = stop()

    }

    companion object {
        val instance = Intake()
    }

    override fun zeroSensors() { }
}