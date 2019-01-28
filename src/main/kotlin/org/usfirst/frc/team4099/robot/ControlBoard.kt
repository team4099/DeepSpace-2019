package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.JoystickUtils
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad

class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

    val throttle: Double
        get() = driver.rightTriggerAxis - driver.leftTriggerAxis

    val turn: Double
        get() = driver.leftXAxis

    val switchToHighGear: Boolean
        get() = driver.rightShoulderButton

    val switchToLowGear: Boolean
        get() = driver.leftShoulderButton

    val toggleIntake: Boolean
        get() = operator.dPadLeft
    val reverseIntakeSlow: Boolean
        get() = operator.bButton

    val reverseIntakeFast: Boolean
        get() = operator.yButton

    val runIntake: Boolean
        get() = operator.aButton

    val actuateFrontClimb: Boolean
        get() = driver.dPadUp
    val actuateBackClimb: Boolean
        get() = driver.dPadUp

    val toggleGrabber: Boolean
        get() = operator.leftShoulderButton

    val moveUp: Boolean
        get() = operator.dPadUp

    val moveDown: Boolean
        get() = operator.dPadDown

    val toggle: Boolean
        get() = operator.rightShoulderButton

    companion object {
        val instance = ControlBoard()
    }



}
