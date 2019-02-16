package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.lib.util.Utils
import org.usfirst.frc.team4099.robot.subsystems.Elevator

class MoveElevatorPositionAction(wantedPosition: Elevator.ElevatorState):Action {
    private val mElevator = Elevator.instance
    val desPosition = wantedPosition

    override fun start() {
        mElevator.elevatorState = desPosition
    }

    override fun done(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mElevator.setElevatorVelocity(0.0)

    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun isFinished(): Boolean{
        return Utils.around(mElevator.observedElevatorPosition, desPosition.targetPos, 3.0)
    }


}