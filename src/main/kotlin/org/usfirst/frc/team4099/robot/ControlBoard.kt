package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.JoystickUtils
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad

class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

    val throttle: Double
        get() = -driver.rightTriggerAxis + driver.leftTriggerAxis

    val turn: Double
        get() = -driver.leftXAxis

    val switchToHighGear: Boolean
        get() = driver.rightShoulderButton

    val switchToLowGear: Boolean
        get() = driver.leftShoulderButton

    val hatchPExtend : Boolean
        get() = operator.dPadDown

    val hatchPOut: Boolean
        get() = operator.dPadUp


/*    val reverseIntakeSlow: Boolean
        get() = operator.bButton

    val reverseIntakeFast: Boolean
        get() = operator.yButton*/

    val intakePower : Double
        get() = operator.leftYAxis

    val runIntake: Boolean
        get() = operator.aButton

    val actuateFrontClimb: Boolean
        get() = driver.dPadUp

    val actuateBackClimb: Boolean
        get() = driver.dPadDown
  
    val moveUp: Boolean
        get() = operator.dPadUp
/*    val eject : Boolean
        get() = operator.leftShoulderButton*/

    val elevatorLow : Boolean
        get() = operator.aButton

    val elevatorMid : Boolean
        get() = operator.bButton

    val elevatorHigh : Boolean
        get() = operator.yButton

    val toggleIntake: Boolean
        get() = operator.xButton

    val elevatorPower: Double
        get() = (operator.rightTriggerAxis - operator.leftTriggerAxis) * 1

//
//    val moveUp: Boolean
//        get() = operator.dPadUp
//
//    val moveDown: Boolean
//        get() = operator.dPadDown
//

    val togglePistons : Boolean
        get() = operator.aButton

    val toggleWrist : Boolean
        get() = operator.leftJoystickButton


    companion object {
        val instance = ControlBoard()
    }



}
