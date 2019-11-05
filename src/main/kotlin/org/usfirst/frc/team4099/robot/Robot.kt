package org.usfirst.frc.team4099.robot

import edu.wpi.first.cameraserver.CameraServer
import org.usfirst.frc.team4099.lib.util.Utils


import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.AutoModeExecuter
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
//import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.lib.util.CrashTracker
import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator
import org.usfirst.frc.team4099.robot.subsystems.*

class Robot : TimedRobot() {

    private var autoModeExecuter: AutoModeExecuter? = null

    private val drive = Drive.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
  //  private val leds = LED.instance

    private val cheesyDriveHelper = CheesyDriveHelper()

   // private val intake = Intake.instance



    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CameraServer.getInstance().startAutomaticCapture()

            DashboardConfigurator.initDashboard()


            enabledLooper.register(drive.loop)

            enabledLooper.register(BrownoutDefender.instance)

            disabledLooper.register(VoltageEstimator.instance)

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("robotInit", t)
            throw t
        }
    }

    override fun disabledInit() {
        try {
            enabledLooper.stop() // end EnabledLooper
            disabledLooper.start() // start DisabledLooper

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("disabledInit", t)
            throw t
        }

    }

    override fun autonomousInit() {

    }

    override fun teleopInit() {
        try {

            enabledLooper.start()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("teleopInit", t)
            throw t
        }
    }

    override fun disabledPeriodic() {
        try {
            //SmartDashboard.putNumber("Dashboard Test", dashBoardTest * 1.0)
//            dashBoardTest++
//            led.setNumber(3)

            //outputAllToSmartDashboard()
//            wrist.outputToSmartDashboard()

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("disabledPeriodic", t)
            throw t
        }

    }

    override fun autonomousPeriodic() {
        try {
            //outputAllToSmartDashboard()
            updateDashboardFeedback()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("autonomousPeriodic", t)
            throw t
        }
       teleopPeriodic()

    }

    override fun teleopPeriodic() {
        try {

            val driveSig = cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1))
            drive.setVelocitySetpoint(6.0 * controlBoard.throttle + controlBoard.turn, 6.0 * controlBoard.throttle - controlBoard.turn, 0.0, 0.0)

        }


        catch (t: Throwable) {
            CrashTracker.logThrowableCrash("teleopPeriodic", t)
            throw t
        }

    }

    override fun testInit() {
        try {
            enabledLooper.start()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("testInit", t)
            throw t
        }

    }



    override fun testPeriodic(){


    }
    /**
     * Log information from all subsystems onto the SmartDashboard
     */
    private fun outputAllToSmartDashboard() {
        drive.outputToSmartDashboard()
    }

    private fun startLiveWindowMode() {
        //drive.startLiveWindowMode()
    }

    private fun updateLiveWindowTables() {
        //drive.updateLiveWindowTables()
    }

    private fun updateDashboardFeedback() {
        // update things such as "is robot aligned with peg"
    }
}
