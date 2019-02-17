package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.robot.subsystems.Intake

class HatchPanelExtendAction: Action {
    private val mIntake = Intake.instance
    override fun start() {
        mIntake.extended
    }

    override fun done() {
    }

    override fun update() {

    }
    override fun isFinished(): Boolean {
        return true
    }
}