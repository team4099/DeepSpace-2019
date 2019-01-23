package org.usfirst.frc.team4099.auto.modes

import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.actions.FollowPathAction
import org.usfirst.frc.team4099.auto.paths.FieldPaths

class HatchPanelOnly(private val startingPosition: DashboardConfigurator.StartingPosition, private val delay: Double) : AutoModeBase() {
    override fun routine(){
        if (startingPosition == DashboardConfigurator.StartingPosition.LEFT){
            runAction(FollowPathAction(FieldPaths.LEFTH2_TO_LEFTROCKET3))
        }
        else if (startingPosition == DashboardConfigurator.StartingPosition.RIGHT){
            runAction(FollowPathAction(FieldPaths.RIGHTH2_TO_RIGHTROCKET3))
        }
    }
}