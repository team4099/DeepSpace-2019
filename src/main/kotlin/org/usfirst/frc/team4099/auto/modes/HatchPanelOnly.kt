package org.usfirst.frc.team4099.auto.modes

import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.actions.*
import org.usfirst.frc.team4099.auto.paths.FieldPaths
import org.usfirst.frc.team4099.robot.subsystems.Vision

class HatchPanelOnly(private val startingPosition: DashboardConfigurator.StartingPosition, private val delay: Double, private val inst : Vision) : AutoModeBase() {
    override fun routine(){
//        if (startingPosition == DashboardConfigurator.StartingPosition.LEFT){
//            runAction(FollowPathAction(FieldPaths.RIGHTH1_TO_RIGHTROCKET1))
//        }
//        else if (startingPosition == DashboardConfigurator.StartingPosition.RIGHT){
            runAction(MoveWristAction())
            runAction(FollowPathAction(FieldPaths.RIGHTH1_TO_RIGHTROCKET1))
            runAction(GoToTargetVisionAction(inst))
//        }
//        runAction(WaitAction(1.0))
//        runAction(BackwardsDistanceAction(24.0))
            //runAction(TurnAction(180.0))
//        if (startingPosition == DashboardConfigurator.StartingPosition.LEFT){
//            runAction(FollowPathAction(FieldPaths.LEFTROCKET3_TO_LOADSTATION))
//        }
//        else if (startingPosition == DashboardConfigurator.StartingPosition.RIGHT){
           // runAction(FollowPathAction(FieldPaths.RIGHTROCKET1_TO_LOADSTATION))
//        }
    }
}