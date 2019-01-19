package org.usfirst.frc.team4099.robot

import edu.wpi.first.wpilibj.IterativeRobot
import org.usfirst.frc.team4099.lib.util.CrashTracker
import org.usfirst.frc.team4099.robot.subsystems.*
import org.usfirst.frc.team4099.robot.ControlBoard.*

class Robot : IterativeRobot() {

    private val climber = Climber.instance
    private val controls = ControlBoard.instance
    private val elevator = Elevator.instance
    private val drive = Drive.instance
    private val grabber = Grabber.instance
    private val controlboard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
  
    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CameraServer.getInstance().startAutomaticCapture()
            CrashTracker.logRobotInit()

            DashboardConfigurator.initDashboard()

            enabledLooper.register(grabber.loop)

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
            val frontToggle = controls.front
            val backToggle = controls.back
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
            val moveDown = controls.moveDown
            val toggle = controls.toggle
            if (operator.moveDown && moveUp) {
                operator.moveDown = false
                elevator.updatePosition(true)
            } else if (!operator.moveDown && moveDown) {
                operator.moveDown = true
                elevator.updatePosition(false)
            }
            if (toggle) {
                elevator.toggleOuttakeMode()
            }
            if (!grabber.push && controlboard.toggleGrabber) {
                grabber.push = true
                println("Pushing the hatch-ey boi")
            } else {
                grabber.push = false
            }




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
