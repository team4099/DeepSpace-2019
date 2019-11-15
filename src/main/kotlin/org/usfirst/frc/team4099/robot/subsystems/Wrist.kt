package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import org.usfirst.frc.team4099.lib.util.conversions.ElevatorConversion
import org.usfirst.frc.team4099.lib.util.conversions.WristConversion
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop
import javax.naming.ldap.Control
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
    private val slave = CANMotorControllerFactory.createPermanentSlaveVictor(Constants.Wrist.WRIST_SLAVE_VICTOR_ID, talon)
//    private val slave = CANMotorControllerFactory.createPermanentSlaveTalon(Constants.Wrist.WRIST_SLAVE_VICTOR_ID, Constants.Wrist.WRIST_TALON_ID)
    //^^^^ TALON FOR PRACTICE BOT CHANGE CHANGE CHANGE

    var wristState = WristState.OPEN_LOOP
    private var wristPower = 0.0
    private var wristAngle = 0.0
    private var lastVelControlPosition = 0.0
    var observedVelocity = 0.0
    var maxVel = 0.0
//    private var outOfBounds: Boolean = true
//        get() = talon.motorOutputPercent > 0 && talon.sensorCollection.quadraturePosition < 0 ||
//                talon.motorOutputPercent < 0 && talon.sensorCollection.quadraturePosition > 1600

    enum class WristState(val targetAngle: Double) {
        HORIZONTAL(-24.1),
        VERTICAL(-2.0),
        CARGO(-29.7),
        OPEN_LOOP(Double.NaN),
        VELOCITY_CONTROL(Double.NaN)
        //TODO Calibrate values
    }

    init {
//        talon.configFactoryDefault()
//        slave.configFactoryDefault()
        talon.set(ControlMode.PercentOutput, 0.0)
        talon.inverted = true
        slave.inverted = true
        talon.setSensorPhase(false)
        //talon.configPeakCurrentLimit(20)
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

        talon.config_kP(2, Constants.Wrist.WRiST_VELOCITY_KP, 0)
        talon.config_kI(2, Constants.Wrist.WRiST_VELOCITY_KI, 0)
        talon.config_kD(2, Constants.Wrist.WRiST_VELOCITY_KD, 0)
        talon.config_kF(2, Constants.Wrist.WRiST_VELOCITY_KF, 0)
        talon.configMaxIntegralAccumulator(2,0.0,0)
        talon.config_IntegralZone(2,1,0)

        talon.configMotionCruiseVelocity(4500, 0)
        talon.configMotionAcceleration(5500, 0)

        zeroSensors()
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
        SmartDashboard.putString("wrist/wristState", wristState.toString())
        SmartDashboard.putNumber("wrist/wristAngle", wristAngle)
        SmartDashboard.putBoolean("wrist/wristUp", wristAngle > PI / 4)
        SmartDashboard.putNumber("wrist/wristSpeed", talon.sensorCollection.quadratureVelocity.toDouble())
    }
       @Synchronized override fun stop() {
        talon.set(ControlMode.Velocity,0.0)
        talon.setNeutralMode(NeutralMode.Coast)
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

    fun setWristPosition(radians: Double){
        talon.configPeakOutputReverse(-0.3, 0)
        talon.configPeakOutputForward(0.3, 0)
        if(radians > wristAngle) {
            talon.selectProfileSlot(0, 0)
        } else {
            talon.selectProfileSlot(1, 0)
        }
        talon.set(ControlMode.MotionMagic, WristConversion.radiansToPulses(radians))

    }

    fun setWristVelocity(radiansPerSecond: Double) {
//        if ((radiansPerSecond <= 0 || Utils.around(radiansPerSecond, 0.0, .1)) && talon.sensorCollection.quadraturePosition < 2.5) {
//            setOpenLoop(0.0)
//            println("wrist exiting at 0 power, $radiansPerSecond")
//            return
//        }
        talon.configPeakOutputReverse(-0.36, 0)
        talon.configPeakOutputForward(0.36, 0)
        if(radiansPerSecond == 0.0){
            setWristPosition(lastVelControlPosition)   //use when pids are better
        }
        else{
            lastVelControlPosition = wristAngle
            wristState = WristState.VELOCITY_CONTROL
            if(radiansPerSecond > 0) {
                talon.selectProfileSlot(2, 0)
            } else {
                talon.selectProfileSlot(2, 0)
            }
            talon.set(ControlMode.Velocity, WristConversion.radiansToPulses(radiansPerSecond))
        }
        //talon.set(ControlMode.Velocity, WristConversion.radiansToPulses(radiansPerSecond))

        //println("nativeVel: $radiansPerSecond, observedVel: ${talon.sensorCollection.quadratureVelocity}, error: ${talon.sensorCollection.quadratureVelocity - radiansPerSecond}")

    }


    val loop: Loop = object : Loop {
        override fun onStart() {
            wristState = WristState.VELOCITY_CONTROL
            talon.setNeutralMode(NeutralMode.Coast)
            print("onStart-------------------------------------------------------------------------------------------------")
        }

        override fun onLoop() {
            synchronized(this@Wrist) {
                wristAngle = WristConversion.pulsesToRadians(talon.sensorCollection.quadraturePosition.toDouble())
                observedVelocity = WristConversion.pulsesToRadians(talon.sensorCollection.quadratureVelocity.toDouble())
                if(Math.abs(observedVelocity) > Math.abs(maxVel)){
                    maxVel = observedVelocity
                }
                //println("Max WristV = " + maxVel)
                //println("IAccumulator: " + talon.integralAccumulator)
                println("Wrist Angle: " + wristAngle)
                if (wristState == WristState.OPEN_LOOP || wristState == WristState.VELOCITY_CONTROL) {
                    //println("Wrist: " + wristAngle)
//                    println("Target: " + wristState.targetAngle)
                    return
                }
//                if (outOfBounds()) {
//                    wristPower = 0.0
//                    talon.set(ControlMode.PercentOutput, 0.0)
//                    return
//                }
                else {
                    setWristPosition(wristState.targetAngle)
                    print("MOTION MAGIC ----------------------------------------------")
                }
//                println("Wrist: " + wristAngle)
//                println("Target: " + wristState.targetAngle)

            }
        }

        override fun onStop(){
            stop()
        }

    }

    override fun zeroSensors() {
        talon.inverted = false
        slave.inverted = false
        talon.setSensorPhase(false)
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