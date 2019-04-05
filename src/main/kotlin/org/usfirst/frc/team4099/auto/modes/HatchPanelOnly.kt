package org.usfirst.frc.team4099.auto.modes

import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.actions.BackwardsDistanceAction
import org.usfirst.frc.team4099.auto.actions.FollowPathAction
import org.usfirst.frc.team4099.auto.actions.WaitAction
import org.usfirst.frc.team4099.auto.paths.FieldPaths

class HatchPanelOnly(private val startingPosition: DashboardConfigurator.StartingPosition, private val delay: Double) : AutoModeBase() {
    override fun routine(){
        if (startingPosition == DashboardConfigurator.StartingPosition.LEFT){
            runAction(FollowPathAction(FieldPaths.RIGHTH2_TO_RIGHTROCKET1))
        }
        else if (startingPosition == DashboardConfigurator.StartingPosition.RIGHT){
            runAction(FollowPathAction(FieldPaths.RIGHTH2_TO_RIGHTROCKET3))
        }
//        runAction(WaitAction(1.0))
//        runAction(BackwardsDistanceAction(24.0))
//        if (startingPosition == DashboardConfigurator.StartingPosition.LEFT){
//            runAction(FollowPathAction(FieldPaths.LEFTROCKET3_TO_LOADSTATION))
//        }
//        else if (startingPosition == DashboardConfigurator.StartingPosition.RIGHT){
//            runAction(FollowPathAction(FieldPaths.RIGHTROCKET3_TO_LOADSTATION))
//        }
    }
}