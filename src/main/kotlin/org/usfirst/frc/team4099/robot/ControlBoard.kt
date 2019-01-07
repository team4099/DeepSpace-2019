package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.JoystickUtils
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad

class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)


    val moveUp: Boolean
        get() = operator.DPadUp

    val moveDown: Boolean
        get() = operator.DPadDown

    val toggle: Boolean
        get() = operator.RightShoulder


    companion object {
        val instance = ControlBoard()
    }

}
