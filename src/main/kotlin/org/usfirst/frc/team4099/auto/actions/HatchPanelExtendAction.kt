package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.robot.subsystems.Intake

class HatchPanelExtendAction: Action {
    private val mIntake = Intake.instance
    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mIntake.extended
    }

    override fun done() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}