package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad


class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

    val throttle: Double
        get() = (-driver.rightTriggerAxis + driver.leftTriggerAxis) * -1.0

    val turn: Double
        get() = -driver.leftXAxis

    val switchToHighGear: Boolean
        get() = driver.rightShoulderButton

    val switchToLowGear: Boolean
        get() = driver.leftShoulderButton

    val hatchPExtend : Boolean
        get() = operator.rightYAxis < -0.2 //change back to operator

    val hatchPOut: Boolean      //refactor
        get() = operator.bButton
    val hatchPDextend : Boolean
        get() = operator.rightYAxis > 0.2 //change back to operator

    val hatchDePOut: Boolean      //refactor
        get() = operator.dPadRight
    val holdCargoL: Boolean
        get() = operator.bButton


    val reverseIntakeSlow: Boolean
        get() = operator.bButton

    val reverseIntakeFast: Boolean
        get() = operator.xButton

    val intakePower : Double
        get() = operator.rightYAxis

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
        get() = operator.dPadDown

    val elevatorMid : Boolean
        get() = operator.dPadRight

    val elevatorHigh : Boolean
        get() = operator.dPadUp

    val toggleIntake: Boolean
        get() = operator.xButton
    val hatchPanelMode: Boolean
        get() = operator.leftShoulderButton
    val cargoMode: Boolean
        get() = operator.rightShoulderButton

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
        get() = operator.leftShoulderButton
    val wristPower: Double
        get() = operator.leftYAxis

    val aimingOn: Boolean
        get() = driver.bButton

    val aimingOff: Boolean
        get() = driver.aButton

    companion object {
        val instance = ControlBoard()
    }



}
