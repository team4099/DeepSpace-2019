package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.JoystickUtils
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad

class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

    val toggleGrabber: Boolean
        get() = operator.leftShoulderButton

    val grab : Boolean
        get() = operator.rightShoulderButton

    val eject : Boolean
        get() = operator.leftShoulderButton

    val stopGrabber : Boolean
        get() = operator.aButton


    companion object {
        val instance = ControlBoard()
    }

}
