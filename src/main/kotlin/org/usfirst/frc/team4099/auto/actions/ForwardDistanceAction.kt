package org.usfirst.frc.team4099.auto.actions

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.lib.drive.DriveSignal
import org.usfirst.frc.team4099.lib.util.Utils
import org.usfirst.frc.team4099.robot.subsystems.Drive


class ForwardDistanceAction(initInchesToMove: Double) : Action {
    private val mDrive: Drive = Drive.instance
    private val inchesToMove: Double = Math.abs(initInchesToMove)
    // private var power: Double  = 1.0


    override fun isFinished(): Boolean {
        return mDrive.getLeftDistanceInches() > 0.85 *inchesToMove && mDrive.getRightDistanceInches() > 0.85 *inchesToMove
    }

    override fun update() {

    }

    override fun done() {
        mDrive.setOpenLoop(DriveSignal.NEUTRAL)
        println("------- END FORWARD -------")
    }

    override fun start() {
        mDrive.resetEncoders()
        mDrive.setPositionSetpoint(inchesToMove, inchesToMove)

    }
}
