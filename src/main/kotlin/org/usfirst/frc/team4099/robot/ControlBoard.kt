package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.joystick.Gamepad
import org.usfirst.frc.team4099.lib.joystick.XboxOneGamepad


class ControlBoard private constructor() {
    private val driver: Gamepad = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
    private val operator: Gamepad = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

    val throttle: Double
        get() = driver.rightTriggerAxis - driver.leftTriggerAxis

    val turn: Double
        get() = -driver.leftXAxis

    val switchToHighGear: Boolean
        get() = driver.rightShoulderButton

    val switchToLowGear: Boolean
        get() = driver.leftShoulderButton

    val climbToTwo: Boolean
        get() = driver.bButton

    val climbToTwoHalf: Boolean
        get() = driver.yButton

    val climbToThree: Boolean
        get() = driver.xButton

    val stowClimber : Boolean
        get() = driver.aButton

    val climbVeloUp: Boolean
        get() = driver.dPadUp

    val climbVeloDown: Boolean
        get() = driver.dPadDown

    val climbAuto: Boolean
        get() = driver.leftJoystickButton && driver.rightJoystickButton && driver.leftShoulderButton && driver.rightShoulderButton

    val climberDrive : Double
        get() = if(driver.rightShoulderButton) 1.0 else 0.0 - if(driver.leftShoulderButton) 1.0 else 0.0

    val feetExtend: Boolean
        get() = driver.dPadLeft
    val feetRetract: Boolean
        get() = driver.dPadRight
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

    val openHatch : Boolean
        get() = operator.bButton

    val closeHatch : Boolean
        get() = operator.aButton
    val openDeployer: Boolean
        get() = operator.xButton
    val closeDeployer: Boolean
        get() = operator.yButton

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

    val wristCargoIntake: Boolean
        get() = operator.xButton

    val climberPower : Double //openloop testing
        get() = operator.rightYAxis

    val aimingOn: Boolean
        get() = driver.bButton

    val aimingOff: Boolean
        get() = driver.aButton

    companion object {
        val instance = ControlBoard()
    }



}
