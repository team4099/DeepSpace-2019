package org.usfirst.frc.team4099.auto.actions

import edu.wpi.first.wpilibj.Timer
import org.usfirst.frc.team4099.robot.subsystems.Elevator
import org.usfirst.frc.team4099.robot.subsystems.Wrist

class MoveWristAction: Action{
    private val mWrist = Wrist.instance
    private var startTime = 0.0

    override fun isFinished(): Boolean {
        return Timer.getFPGATimestamp() - startTime > Math.abs(2.0)
    }

    override fun update() {
        mWrist.wristState =  Wrist.WristState.HORIZONTAL
    }

    override fun done() {
        mWrist.wristState =  Wrist.WristState.HORIZONTAL
    }

    override fun start() {
        mWrist.wristState =  Wrist.WristState.HORIZONTAL
    }
}