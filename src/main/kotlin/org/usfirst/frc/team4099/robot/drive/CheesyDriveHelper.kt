package org.usfirst.frc.team4099.robot.drive
import org.usfirst.frc.team4099.lib.drive.DriveSignal
import org.usfirst.frc.team4099.lib.util.Utils

class CheesyDriveHelper {

    private var mOldWheel = 0.0
    private var mQuickStopAccumlator = 0.0
    private var mNegInertiaAccumlator = 0.0

    fun curvatureDrive(throttle: Double, wheel: Double, isQuickTurn: Boolean): DriveSignal {
        var throttle = throttle
        var wheel = wheel

        wheel = handleDeadband(wheel, kWheelDeadband)
        throttle = handleDeadband(throttle, kThrottleDeadband)

        val negInertia = wheel - mOldWheel
        mOldWheel = wheel

        val wheelNonLinearity: Double
        if (false) {
            wheelNonLinearity = kHighWheelNonLinearity
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
        } else {
            wheelNonLinearity = kLowWheelNonLinearity
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
        }

        var leftPwm: Double
        var rightPwm: Double
        val overPower: Double
        val sensitivity: Double

        val angularPower: Double
        val linearPower: Double

        // Negative inertia!
        val negInertiaScalar: Double
        if (false) {
            negInertiaScalar = kHighNegInertiaScalar
            sensitivity = kHighSensitivity
        } else {
            if (wheel * negInertia > 0) {
                // If we are moving away from 0.0, aka, trying to get more wheel.
                negInertiaScalar = kLowNegInertiaTurnScalar
            } else {
                // Otherwise, we are attempting to go back to 0.0.
                if (Math.abs(wheel) > kLowNegInertiaThreshold) {
                    negInertiaScalar = kLowNegInertiaFarScalar
                } else {
                    negInertiaScalar = kLowNegInertiaCloseScalar
                }
            }
            sensitivity = kLowSensitiity
        }
        val negInertiaPower = negInertia * negInertiaScalar
        mNegInertiaAccumlator += negInertiaPower

        wheel = wheel + mNegInertiaAccumlator
        if (mNegInertiaAccumlator > 1) {
            mNegInertiaAccumlator -= 1.0
        } else if (mNegInertiaAccumlator < -1) {
            mNegInertiaAccumlator += 1.0
        } else {
            mNegInertiaAccumlator = 0.0
        }
        linearPower = throttle

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < kQuickStopDeadband) {
                val alpha = kQuickStopWeight
                mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator + alpha * Utils.limit(wheel, 1.0) * kQuickStopScalar
            }
            overPower = 1.0
            angularPower = wheel
        } else {
            overPower = 0.0
            angularPower = Math.abs(throttle) * wheel * sensitivity - mQuickStopAccumlator
            if (mQuickStopAccumlator > 1) {
                mQuickStopAccumlator -= 1.0
            } else if (mQuickStopAccumlator < -1) {
                mQuickStopAccumlator += 1.0
            } else {
                mQuickStopAccumlator = 0.0
            }
        }

        leftPwm = linearPower
        rightPwm = leftPwm
        leftPwm += angularPower
        rightPwm -= angularPower

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0)
            leftPwm = 1.0
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0)
            rightPwm = 1.0
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm)
            leftPwm = -1.0
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm)
            rightPwm = -1.0
        }
        return DriveSignal(leftPwm, rightPwm)
    }

    fun handleDeadband(`val`: Double, deadband: Double): Double {
        return if (Math.abs(`val`) > Math.abs(deadband)) `val` else 0.0
    }

    companion object {
        val instance = CheesyDriveHelper()

        private val kThrottleDeadband = 0.02
        private val kWheelDeadband = 0.02

        // These factor determine how fast the wheel traverses the "non linear" sine curve.
        private val kHighWheelNonLinearity = 0.1
        private val kLowWheelNonLinearity = 0.01

        private val kHighNegInertiaScalar = 4.0

        private val kLowNegInertiaThreshold = 0.0//0.3
        private val kLowNegInertiaTurnScalar = 0.0//1.1
        private val kLowNegInertiaCloseScalar = 0.0//0.3//0.8
        private val kLowNegInertiaFarScalar = 0.0//0.4//1.0

        private val kHighSensitivity = 0.5//0.11
        private val kLowSensitiity = 0.5//0.11

        private val kQuickStopDeadband = 0.0//0.38
        private val kQuickStopWeight = 0.0//0.05
        private val kQuickStopScalar = 0.0//0.65
    }
}