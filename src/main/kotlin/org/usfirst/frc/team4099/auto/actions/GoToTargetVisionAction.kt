package org.usfirst.frc.team4099.auto.actions

import org.usfirst.frc.team4099.lib.drive.DriveSignal
import org.usfirst.frc.team4099.lib.util.Utils
import org.usfirst.frc.team4099.robot.subsystems.Drive
import org.usfirst.frc.team4099.robot.subsystems.Vision
import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems.Superstructure

/**
 * Automated drive to target action
 * Assumes that vision target in already in sight
 */

class GoToTargetVisionAction: Action {
    val mVision = Vision.instance
    val mDrive = Drive.instance
    val cheesyDriveHelper = CheesyDriveHelper()

    //val mStructure = Superstructure.instance

    override fun start(){
        mVision.setState(Vision.VisionState.AIMING)

    }
    override fun update() {
        if (mVision.onTarget) {
            mDrive.setOpenLoop(cheesyDriveHelper.curvatureDrive(0.3, 0.0, false))
        } else if (mVision.visionState != Vision.VisionState.INACTIVE) {
            mDrive.setOpenLoop(cheesyDriveHelper.curvatureDrive(0.3, mVision.steeringAdjust, false))
        }
        //mDrive.setLeftRightPower(0.3 + mVision.steeringAdjust, 0.3 - mVision.steeringAdjust) could add this line

    }
    override fun done(){
        mVision.setState(Vision.VisionState.INACTIVE)
        mDrive.setOpenLoop(DriveSignal.NEUTRAL)

    }
    override fun isFinished(): Boolean {
        return mVision.distance < 5.0

    }

}