package org.usfirst.frc.team4099.robot

import edu.wpi.first.wpilibj.TimedRobot
import org.usfirst.frc.team4099.lib.util.CrashTracker

import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper

import org.usfirst.frc.team4099.robot.drive.TankDriveHelper
import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator

import org.usfirst.frc.team4099.robot.subsystems.*

class Robot : TimedRobot() {


    private val climber = Climber.instance
    private val controls = ControlBoard.instance
    private val elevator = Elevator.instance
    private val drive = Drive.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
    private val cheesyDriveHelper = CheesyDriveHelper()
  

    private val intake = Intake.instance




    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CrashTracker.logRobotInit()

//            DashboardConfigurator.initDashboard()

            enabledLooper.register(intake.loop)

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


            val frontToggle = controls.actuateFrontClimb
            val backToggle = controls.actuateBackClimb
            if (frontToggle && climber.climberState == Climber.ClimberState.FRONT_DOWN) {
                climber.climberState = Climber.ClimberState.BOTH_UP

            } else if (frontToggle && climber.climberState == Climber.ClimberState.BOTH_UP) {
                climber.climberState = Climber.ClimberState.FRONT_DOWN

            } else if (backToggle && climber.climberState == Climber.ClimberState.BOTH_UP) {
                climber.climberState = Climber.ClimberState.BACK_DOWN

            } else if (backToggle && climber.climberState == Climber.ClimberState.BACK_DOWN) {
                climber.climberState = Climber.ClimberState.BOTH_UP
            }

            val moveUp = controls.moveUp
            var moveDown = controls.moveDown
            val toggle = controls.toggle

            if (moveDown && moveUp) {
                moveDown = false
                elevator.updatePosition(true)
            } else if (!moveDown && moveDown) {
                moveDown = true
            }

            if (controls.moveDown && moveUp) {
                elevator.updatePosition(true)
            } else if (!controls.moveDown && moveDown) {
                elevator.updatePosition(false)
            }
            if (toggle) {
                elevator.toggleOuttakeMode()
            }
            if (!grabber.push && controlBoard.toggleGrabber) {
                grabber.push = true
                println("Pushing the hatch-ey boi")
            } else {
                grabber.push = false
            }


            if (intake.up && controlBoard.toggleIntake) {
                intake.up = false
                println("Lowering intake")
            } else if (!intake.up && controlBoard.toggleIntake) {
                intake.up = true
                println("Raising intake")
            }

            intake.intakeState = when {
                controlBoard.reverseIntakeFast -> Intake.IntakeState.FAST_OUT
                controlBoard.reverseIntakeSlow -> Intake.IntakeState.SLOW_OUT
                controlBoard.runIntake -> Intake.IntakeState.IN
                intake.intakeState != Intake.IntakeState.SLOW -> Intake.IntakeState.STOP
                else -> intake.intakeState
            }

            if (drive.highGear && controlBoard.switchToLowGear) {
                drive.highGear = false
                println("Shifting to low gear")
            } else if (!drive.highGear && controlBoard.switchToHighGear) {
                drive.highGear = true
                println("Shifting to high gear")
            }
            drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controls.throttle, controls.turn, controls.throttle == 0.0))


            outputAllToSmartDashboard()
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
