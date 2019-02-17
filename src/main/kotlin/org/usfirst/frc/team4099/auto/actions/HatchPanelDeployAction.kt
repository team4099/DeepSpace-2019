package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.robot.subsystems.Intake

class HatchPanelDeployAction: Action {
    private val mIntake = Intake.instance
    override fun start() {
        mIntake.hatchOut = true
    }

    override fun done() {
    }

    override fun update() {

    }
    override fun isFinished(): Boolean {
        return true
    }
}