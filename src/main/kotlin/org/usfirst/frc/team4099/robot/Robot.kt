package org.usfirst.frc.team4099.robot

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.CameraServer
import org.usfirst.frc.team4099.DashboardConfigurator
//import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.lib.util.CrashTracker

import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import org.usfirst.frc.team4099.robot.drive.TankDriveHelper
import org.usfirst.frc.team4099.lib.util.Utils
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator

import org.usfirst.frc.team4099.robot.subsystems.*
import src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems.Superstructure

class Robot : TimedRobot() {
    private val vision = Vision.instance


    //private val climber = Climber.instance

    private val wrist = Wrist.instance
    private val intake = Intake.instance

    private val elevator = Elevator.instance
    private val drive = Drive.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
   // private val elevator = Elevator.instance
    private val superstructure = Superstructure.instance
    private val cheesyDriveHelper = CheesyDriveHelper()
  

   // private val intake = Intake.instance





    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CrashTracker.logRobotInit()

            DashboardConfigurator.initDashboard()
//            enabledLooper.register(drive.loop)

            enabledLooper.register(intake.loop)
      //      enabledLooper.register(intake.loop)
            //enabledLooper.register(intake.loop)
//            enabledLooper.register(superstructure.loop)



            enabledLooper.register(wrist.loop)

            //enabledLooper.register(drive.loop)

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
            enabledLooper.register(superstructure.loop)
            enabledLooper.register(drive.loop)
            enabledLooper.register(vision.loop)
            enabledLooper.start()
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


            if (!intake.pistonsOut && controlBoard.togglePistons) {
                intake.pistonsOut = true
                println("Pushing the hatch-ey boi")
            } else {
                intake.pistonsOut = false
            }

            intake.intakeState = when {
//                controlBoard.reverseIntakeFast -> Intake.IntakeState.FAST_OUT
//                controlBoard.reverseIntakeSlow -> Intake.IntakeState.SLOW_OUT
                controlBoard.runIntake -> Intake.IntakeState.IN
                intake.intakeState != Intake.IntakeState.SLOW -> Intake.IntakeState.STOP
                else -> intake.intakeState
            }

//            if(controlBoard.toggleWrist){
//                wrist.setWristMode(if(wrist.wristState == Wrist.WristState.VERTICAL) Wrist.WristState.HORIZONTAL else Wrist.WristState.VERTICAL)
//            }

//            val wantedVelocity = controlBoard.elevatorPower * Constants.Elevator.MAX_SPEED
//            if (Math.abs(controlBoard.elevatorPower) > Constants.Elevator.MIN_TRIGGER) {
//                elevator.setElevatorVelocity(wantedVelocity)
//                elevator.elevatorState = Elevator.ElevatorState.VELOCITY_CONTROL
//            }
//            else {
//                elevator.setElevatorVelocity(0)
//            }
//            val frontToggle = controlBoard.actuateFrontClimb
//            val backToggle = controlBoard.actuateBackClimb
//            if (frontToggle && climber.climberState == Climber.ClimberState.FRONT_DOWN) {
//                climber.climberState = Climber.ClimberState.BOTH_UP
//
//            } else if (frontToggle && climber.climberState == Climber.ClimberState.BOTH_UP) {
//                climber.climberState = Climber.ClimberState.FRONT_DOWN
//
//            } else if (backToggle && climber.climberState == Climber.ClimberState.BOTH_UP) {
//                climber.climberState = Climber.ClimberState.BACK_DOWN
//
//            } else if (backToggle && climber.climberState == Climber.ClimberState.BACK_DOWN) {
//                climber.climberState = Climber.ClimberState.BOTH_UP
//            }
//
//            val moveUp = controlBoard.moveUp
//            var moveDown = controlBoard.moveDown
//            val toggle = controlBoard.toggle
//
//            if (controlBoard.moveDown && moveUp) {
//                elevator.updatePosition(true)
//            } else if (!controlBoard.moveDown && moveDown) {
//                elevator.updatePosition(false)
//            }
//            if (toggle) {
//                elevator.toggleOuttakeMode()
//            }
//            if (!grabber.push && controlBoard.toggleGrabber) {
//                grabber.push = true
//                println("Pushing the hatch-ey boi")
//            } else {
//                grabber.push = false
//            }
//            if(controlBoard.grab) {
//                grabber.intakeState = Grabber.IntakeState.IN
//            }
//            if(controlBoard.eject){
//                grabber.intakeState = Grabber.IntakeState.OUT
//            }
//            if(controlBoard.stopGrabber){
//                grabber.intakeState = Grabber.IntakeState.NEUTRAL
//            }
//
//
//            if (intake.up && controlBoard.toggleIntake) {
//                intake.up = false
//                println("Lowering intake")
//            } else if (!intake.up && controlBoard.toggleIntake) {
//                intake.up = true
//                println("Raising intake")
//            }
//
//            intake.intakeState = when {
//                controlBoard.reverseIntakeFast -> Intake.IntakeState.FAST_OUT
//                controlBoard.reverseIntakeSlow -> Intake.IntakeState.SLOW_OUT
//                controlBoard.runIntake -> Intake.IntakeState.IN
//                intake.intakeState != Intake.IntakeState.SLOW -> Intake.IntakeState.STOP
//                else -> intake.intakeState
//            }
//
//            if (drive.highGear && controlBoard.switchToLowGear) {
//                drive.highGear = false
//                println("Shifting to low gear")
//            } else if (!drive.highGear && controlBoard.switchToHighGear) {
//                drive.highGear = true
//                println("Shifting to high gear")
//            }
            drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))

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
        //drive.startLiveWindowMode()
    }

    private fun updateLiveWindowTables() {
        //drive.updateLiveWindowTables()
    }

    private fun updateDashboardFeedback() {
        // update things such as "is robot aligned with peg"
    }
}
