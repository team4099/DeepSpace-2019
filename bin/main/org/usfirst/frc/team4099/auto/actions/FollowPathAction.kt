package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.auto.paths.FieldPaths
import org.usfirst.frc.team4099.auto.paths.Path
import org.usfirst.frc.team4099.lib.drive.DriveSignal
import org.usfirst.frc.team4099.robot.subsystems.Drive

class FollowPathAction(path: FieldPaths): Action {
    private val mDrive: Drive = Drive.instance
    private val pathF = Path(path)

    override fun start() {
        mDrive.enablePathFollow(pathF)
    }
    override fun update(){

    }
    override fun done(){
        mDrive.setOpenLoop(DriveSignal.NEUTRAL)
    }
    override fun isFinished(): Boolean{
        return mDrive.isPathFinished()

    }
}