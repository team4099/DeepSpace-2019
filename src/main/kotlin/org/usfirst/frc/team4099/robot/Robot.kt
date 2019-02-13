package org.usfirst.frc.team4099.robot

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import org.usfirst.frc.team4099.lib.util.CrashTracker

import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import org.usfirst.frc.team4099.robot.drive.TankDriveHelper
import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.lib.util.Utils
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator


import org.usfirst.frc.team4099.robot.subsystems.*

class Robot : TimedRobot() {


    //private val climber = Climber.instance
    private val elevator = Elevator.instance
    private val drive = Drive.instance
    //private val grabber = Grabber.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
    private val cheesyDriveHelper = CheesyDriveHelper()

   // private val intake = Intake.instance





    init {
        CrashTracker.logRobotConstruction()
        CameraServer.getInstance().startAutomaticCapture()
    }


    override fun robotInit() {
        try {
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("robotInit", t)
            throw t
        }
    }

    override fun disabledInit() {
        try {

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("disabledInit", t)
            throw t
        }

    }

    override fun autonomousInit() {
        try {


        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("autonomousInit", t)
            throw t
        }

    }

    override fun teleopInit() {
        try {

            CrashTracker.logRobotInit()

//            DashboardConfigurator.initDashboard()


             enabledLooper.register(elevator.loop)

//            enabledLooper.register(intake.loop)

            enabledLooper.register(drive.loop)

            enabledLooper.register(BrownoutDefender.instance)

            disabledLooper.register(VoltageEstimator.instance)

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("teleopInit", t)
            throw t
        }

    }

    override fun disabledPeriodic() {
        try {
            outputAllToSmartDashboard()

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("disabledPeriodic", t)
            throw t
        }

    }

    override fun autonomousPeriodic() {
        try {
            outputAllToSmartDashboard()
            updateDashboardFeedback()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("autonomousPeriodic", t)
            throw t
        }

    }

    override fun teleopPeriodic() {
        try {
//            climberStateEntry.setString(climber.climberState.toString)
//            elevatorPositionEntry.setDouble(elevator.observedElevatorPosition)
//            intakeStateEntry.setString(if(intake.intakeState == Intake.IntakeState.IN) "In" else "Out")
//            intakeModeEntry.setString(if(elevator.isHatchPanel) "Hatch" else "Cargo")
//            value ++
            drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
            DashboardConfigurator.updateValues()

            //outputAllToSmartDashboard()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("teleopPeriodic", t)
            throw t
        }

    }

    override fun testInit() {
        try {

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("testInit", t)
            throw t
        }

    }



    override fun testPeriodic() = teleopPeriodic()

    /**
     * Log information from all subsystems onto the SmartDashboard
     */
    private fun outputAllToSmartDashboard() {

    }

    private fun startLiveWindowMode() {
        drive.startLiveWindowMode()
    }

    private fun updateLiveWindowTables() {
        drive.updateLiveWindowTables()
    }

    private fun updateDashboardFeedback() {
        // update things such as "is robot aligned with peg"
    }
}
