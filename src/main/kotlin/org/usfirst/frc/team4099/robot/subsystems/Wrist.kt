package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import org.usfirst.frc.team4099.lib.util.conversions.WristConversion
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop
import kotlin.math.*

/**
 * @author Team 4099
 *
 * This class is the constructor for the Wrist subsystem
 *
 * @constructor Creates the Wrist subsystem
 *
 */

class Wrist private constructor(): Subsystem {
    private val talon = CANMotorControllerFactory.createDefaultTalon(Constants.Wrist.WRIST_TALON_ID)
    //private val slave = CANMotorControllerFactory.createPermanentSlaveVictor(Constants.Wrist.WRIST_SLAVE_VICTOR_ID, talon)
    private val slave = CANMotorControllerFactory.createPermanentSlaveTalon(Constants.Wrist.WRIST_SLAVE_VICTOR_ID, Constants.Wrist.WRIST_TALON_ID)
    //^^^^ TALON FOR PRACTICE BOT CHANGE CHANGE CHANGE
//    private val arm = Arm.instance

    var wristState = WristState.OPEN_LOOP
    private var wristPower = 0.0
    private var wristAngle = 0.0
//    private var outOfBounds: Boolean = true
//        get() = talon.motorOutputPercent > 0 && talon.sensorCollection.quadraturePosition < 0 ||
//                talon.motorOutputPercent < 0 && talon.sensorCollection.quadraturePosition > 1600

    enum class WristState(val targetAngle: Double) {
        HORIZONTAL(-29.9),
        VERTICAL(40.5),
        OPEN_LOOP(Double.NaN),
         VELOCITY_CONTROL(Double.NaN)
        //TODO Calibrate values
    }

    init {
        talon.set(ControlMode.PercentOutput, 0.0)
        talon.inverted = true
        slave.inverted = false
        talon.setSensorPhase(true)
        talon.configPeakCurrentLimit(20)

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
        talon.configNominalOutputForward(0.0, 0)
        talon.configNominalOutputReverse(0.0, 0)
        talon.configPeakOutputReverse(-0.3, 0)
        talon.configPeakOutputForward(0.3, 0)

        talon.config_kP(0, Constants.Wrist.WRIST_UP_KP, 0)
        talon.config_kI(0, Constants.Wrist.WRIST_UP_KI, 0)
        talon.config_kD(0, Constants.Wrist.WRIST_UP_KD, 0)
        talon.config_kF(0, Constants.Wrist.WRIST_UP_KF, 0)
        talon.configMaxIntegralAccumulator(0,0.0,0)
        talon.config_IntegralZone(0,1,0)

        talon.config_kP(1, Constants.Wrist.WRIST_DOWN_KP, 0)
        talon.config_kI(1, Constants.Wrist.WRIST_DOWN_KI, 0)
        talon.config_kD(1, Constants.Wrist.WRIST_DOWN_KD, 0)
        talon.config_kF(1, Constants.Wrist.WRIST_DOWN_KF, 0)
        talon.configMaxIntegralAccumulator(1,0.0,0)
        talon.config_IntegralZone(1,1,0)

        talon.configMotionCruiseVelocity(40, 0)
        talon.configMotionAcceleration(100, 0)

//        talon.configForwardSoftLimitEnable(true, 0)
//        talon.configForwardSoftLimitThreshold(100, 0)
//        talon.configReverseSoftLimitEnable(true, 0)
//        talon.configReverseSoftLimitThreshold(0, 0)
//        talon.overrideSoftLimitsEnable(true)
//        talon.overrideLimitSwitchesEnable(true)
    }

    /**
     * Outputs the angle of the wrist
     */
    override fun outputToSmartDashboard() {
        //SmartDashboard.putNumber("wrist/wristAngle", wristAngle)
        //SmartDashboard.putBoolean("wrist/wristUp", wristAngle > PI / 4)
        //SmartDashboard.putNumber("wrist/wristSpeed", talon.sensorCollection.quadratureVelocity.toDouble())
    }

    @Synchronized override fun stop() {
//        setWristMode(WristState.HORIZONTAL)
    }

    /**
     * Sets the state of the Arm
     *
     * @param state is the wrist state
     */
    fun setWristMode(state: WristState) {
        wristState = state
    }

    fun getWristPosition() : Double {
        return WristConversion.pulsesToRadians(talon.sensorCollection.pulseWidthPosition.toDouble())
    }

    fun setOpenLoop(power: Double) {
        wristState = WristState.OPEN_LOOP
        wristPower = power
        talon.set(ControlMode.PercentOutput, wristPower)
//        println("wrist speed: ${talon.sensorCollection.quadratureVelocity}")
    }

    fun setWristVelocity(radiansPerSecond: Double) {
//        if ((radiansPerSecond <= 0 || Utils.around(radiansPerSecond, 0.0, .1)) && talon.sensorCollection.quadraturePosition < 2.5) {
//            setOpenLoop(0.0)
//            println("wrist exiting at 0 power, $radiansPerSecond")
//            return
//        }
        wristState = WristState.VELOCITY_CONTROL
        if(radiansPerSecond > 0) {
            talon.selectProfileSlot(1, 0)
        } else {
            talon.selectProfileSlot(0, 0)
        }
        talon.set(ControlMode.Velocity, radiansPerSecond)
        //println("nativeVel: $radiansPerSecond, observedVel: ${talon.sensorCollection.quadratureVelocity}, error: ${talon.sensorCollection.quadratureVelocity - radiansPerSecond}")

    }


    val loop: Loop = object : Loop {
        override fun onStart() {
           zeroSensors()
            wristState = WristState.OPEN_LOOP
            print("onStart-------------------------------------------------------------------------------------------------")
        }

        override fun onLoop() {
            synchronized(this@Wrist) {
                println("hi")
                wristAngle = WristConversion.pulsesToRadians(talon.sensorCollection.quadraturePosition.toDouble())
                //println("IAccumulator: " + talon.integralAccumulator)
                println("Wrist: " + wristAngle)
                if (wristState == WristState.OPEN_LOOP || wristState == WristState.VELOCITY_CONTROL) {
                    //println("Wrist: " + wristAngle)
                    //println("Target: " + wristState.targetAngle)
                    return
                }
//                if (outOfBounds()) {
//                    wristPower = 0.0
//                    talon.set(ControlMode.PercentOutput, 0.0)
//                    return
//                }
                else {
                    talon.set(ControlMode.MotionMagic, WristConversion.radiansToPulses(wristState.targetAngle).toDouble())
                    print("MOTION MAGIC ----------------------------------------------")
                }
//                println("Wrist: " + wristAngle)
//                println("Target: " + wristState.targetAngle)

            }
        }

        override fun onStop() = stop()

    }

    override fun zeroSensors() {
        talon.inverted = true
        slave.inverted = false
        talon.setSensorPhase(true)
        talon.configPeakCurrentLimit(20)

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
        talon.configNominalOutputForward(0.0, 0)
        talon.configNominalOutputReverse(0.0, 0)
        talon.configPeakOutputReverse(-0.3, 0)
        talon.configPeakOutputForward(0.3, 0)

//        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 20, 0)
//        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 20, 0)

        talon.config_kP(0, Constants.Wrist.WRIST_UP_KP, 0)
        talon.config_kI(0, Constants.Wrist.WRIST_UP_KI, 0)
        talon.config_kD(0, Constants.Wrist.WRIST_UP_KD, 0)
        talon.config_kF(0, Constants.Wrist.WRIST_UP_KF, 0)
        talon.configMaxIntegralAccumulator(0,0.0,0)
        talon.config_IntegralZone(0,1,0)

        talon.config_kP(1, Constants.Wrist.WRIST_DOWN_KP, 0)
        talon.config_kI(1, Constants.Wrist.WRIST_DOWN_KI, 0)
        talon.config_kD(1, Constants.Wrist.WRIST_DOWN_KD, 0)
        talon.config_kF(1, Constants.Wrist.WRIST_DOWN_KF, 0)
        talon.configMaxIntegralAccumulator(1,0.0,0)
        talon.config_IntegralZone(1,1,0)

        talon.configMotionCruiseVelocity(40, 0)
        talon.configMotionAcceleration(100, 0)
        println("zeroed")
        talon.integralAccumulator = 0.0
        talon.setIntegralAccumulator(0.0)
        talon.sensorCollection.setPulseWidthPosition(0,0)
        talon.sensorCollection.setQuadraturePosition(0,0)
        println(talon.sensorCollection.quadraturePosition)
    }

    companion object {
        val instance = Wrist()
    }

}