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

    val reverseCargoIntake: Boolean
        get() = operator.bButton

    val runCargoIntake: Boolean
        get() = operator.aButton

    val holdCargo: Boolean
        get() = operator.xButton

    val climberUp : Boolean
        get() = driver.dPadUp

    val climberDown : Boolean
        get() = driver.dPadDown

    val climberDrive : Boolean
        get() = driver.rightShoulderButton

    val openHatch : Boolean
        get() = operator.bButton

    val closeHatch : Boolean
        get() = operator.aButton

    val pushLatch : Boolean
        get() = driver.yButton

    val closeLatch : Boolean
        get() = driver.xButton

    val elevatorLow : Boolean
        get() = operator.dPadDown

    val elevatorMid : Boolean
        get() = operator.dPadRight

    val elevatorHigh : Boolean
        get() = operator.dPadUp
    val elevatorShip: Boolean
        get() = operator.dPadLeft

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

    val wristPower: Double
        get() = operator.leftYAxis

    val aimingOn: Boolean
        get() = false//driver.bButton

    val aimingOff: Boolean
        get() = false//driver.aButton

    companion object {
        val instance = ControlBoard()
    }



}
