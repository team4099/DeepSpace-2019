package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.JoystickUtils
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad

class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)


    val toggleIntake: Boolean
        get() = operator.dPadLeft
    val reverseIntakeSlow: Boolean
        get() = operator.bButton

    val reverseIntakeFast: Boolean
        get() = operator.yButton

    val runIntake: Boolean
        get() = operator.aButton

    val front: Boolean
        get() = driver.rightShoulderButton
    val back: Boolean
        get() = driver.leftShoulderButton

    val toggleGrabber: Boolean
        get() = operator.leftShoulderButton
  
    val grab : Boolean
        get() = operator.rightShoulderButton

    val eject : Boolean
        get() = operator.leftShoulderButton

    val stopGrabber : Boolean
        get() = operator.aButton

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
