package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.robot.subsystems.Intake
import edu.wpi.first.wpilibj.Timer

class HatchPanelExtendAction: Action {
    private val mIntake = Intake.instance
    private var startTime: Double = 0.toDouble()
    override fun start() {
        //mIntake.extended = true
        startTime = Timer.getFPGATimestamp()
    }

    override fun done() {
    }

    override fun update() {

    }
    override fun isFinished(): Boolean {
        //return Timer.getFPGATimestamp() - startTime >= 0.3
        return true
    }
}