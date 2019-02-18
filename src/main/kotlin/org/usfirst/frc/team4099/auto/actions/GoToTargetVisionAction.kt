package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.lib.drive.DriveSignal
import org.usfirst.frc.team4099.robot.subsystems.Drive
import org.usfirst.frc.team4099.robot.subsystems.Vision
import src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems.Superstructure

/**
 * Automated drive to target action
 * Assumes that vision target in already in sight
 */

class GoToTargetVisionAction: Action {
    val mVision = Vision.instance
    val mDrive = Drive.instance
    //val mStructure = Superstructure.instance

    override fun start(){
        mVision.setState(Vision.VisionState.AIMING)

    }
    override fun update() {
        if (mVision.onTarget) {
            mDrive.setLeftRightPower(0.3, 0.3)
        } else if (mVision.visionState != Vision.VisionState.INACTIVE) {
            mDrive.setLeftRightPower(mVision.steeringAdjust, -mVision.steeringAdjust)
        }
        //mDrive.setLeftRightPower(0.3 + mVision.steeringAdjust, 0.3 - mVision.steeringAdjust) could add this line

    }
    override fun done(){
        mVision.setState(Vision.VisionState.INACTIVE)
        mDrive.setOpenLoop(DriveSignal.NEUTRAL)

    }
    override fun isFinished(): Boolean {
        return mVision.getDistance() < 5.0

    }

}