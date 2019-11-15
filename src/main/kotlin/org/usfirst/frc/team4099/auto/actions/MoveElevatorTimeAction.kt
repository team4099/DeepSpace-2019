package org.usfirst.frc.team4099.auto.actions

import edu.wpi.first.wpilibj.Timer
import org.usfirst.frc.team4099.robot.subsystems.Elevator

class MoveElevatorTimeAction(private val time: Double): Action {
    private val mElevator = Elevator.instance
    private var startTime = 0.0
    private val direction = time / Math.abs(time)

    override fun isFinished(): Boolean {
        return Timer.getFPGATimestamp() - startTime > Math.abs(time)
    }

    override fun update() {
        mElevator.setElevatorVelocity(1800.0 * direction) // change
    }

    override fun done() {
        mElevator.setElevatorVelocity(0.0)
    }

    override fun start() {
        startTime = Timer.getFPGATimestamp()
    }
}