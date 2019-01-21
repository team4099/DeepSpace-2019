package org.usfirst.frc.team4099.robot

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.lib.util.CrashTracker
import org.usfirst.frc.team4099.lib.util.LatchedBoolean
import org.usfirst.frc.team4099.lib.util.ReflectingCSVWriter
import org.usfirst.frc.team4099.lib.util.SignalTable
import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import org.usfirst.frc.team4099.robot.drive.TankDriveHelper
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator
import org.usfirst.frc.team4099.robot.ControlBoard
import org.usfirst.frc.team4099.robot.subsystems.*

class Robot : IterativeRobot() {

    private val drive = Drive.instance
    private val intake = Intake.instance
    private val controlboard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
    private val elevator = Elevator.instance

    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CameraServer.getInstance().startAutomaticCapture()
            CrashTracker.logRobotInit()

            DashboardConfigurator.initDashboard()

            enabledLooper.register(intake.loop)
            enabledLooper.register(intake.loop)

            enabledLooper.register(BrownoutDefender.instance)

            disabledLooper.register(VoltageEstimator.instance)
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

            if (intake.up && controlboard.lowerIntake) {
                intake.up = false
                println("Lowering intake")
            } else if (!intake.up && controlboard.lowerIntake) {
                intake.up = true
                println("Raising intake")
            }

            intake.intakeState = when {
                controlboard.reverseIntakeFast -> Intake.IntakeState.FAST_OUT
                controlboard.reverseIntakeSlow -> Intake.IntakeState.SLOW_OUT
                controlboard.runIntake -> Intake.IntakeState.IN
                intake.intakeState != Intake.IntakeState.SLOW -> Intake.IntakeState.STOP
                else -> intake.intakeState
            val moveUp = controls.moveUp
            val moveDown = controls.moveDown
            val toggle = controls.toggle

            if (operator.moveDown && moveUp) {
                operator.moveDown = false
            } else if (!operator.moveDown && moveDown) {
                operator.moveDown = true
            }

            elevator.updatePosition()

            if (toggle) {
                elevator.toggleOuttakeMode()

            }

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
