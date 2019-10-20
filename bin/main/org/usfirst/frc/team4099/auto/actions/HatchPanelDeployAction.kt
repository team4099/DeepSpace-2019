package org.usfirst.frc.team4099.auto.actions

import edu.wpi.first.wpilibj.Timer
import org.usfirst.frc.team4099.robot.subsystems.Intake

class HatchPanelDeployAction: Action {
    private val mIntake = Intake.instance
    private var startTime: Double = 0.toDouble()
    override fun start() {
        //mIntake.hatchOut = true
        startTime = Timer.getFPGATimestamp()
    }

    override fun done() {
        //mIntake.hatchOut = false
    }

    override fun update() {

    }
    override fun isFinished(): Boolean {
        return Timer.getFPGATimestamp() - startTime >= 0.3
    }
}