package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.util.Utils


import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.auto.modes.HatchPanelOnly
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.TimedRobot
//import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.lib.util.CrashTracker

import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import org.usfirst.frc.team4099.robot.drive.TankDriveHelper
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator

import org.usfirst.frc.team4099.robot.subsystems.*
import src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems.Superstructure

class Robot : TimedRobot() {
    private val vision = Vision.instance

    private var autoModeExecuter: AutoModeExecuter? = null


    private val test3 : DoubleSolenoid = DoubleSolenoid(1,6)
    //private val climber = Climber.instance

    private val wrist = Wrist.instance
    //private val intake = Intake.instance

    private val drive = Drive.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
  //  private val leds = LED.instance
    private val elevator = Elevator.instance
    private val superstructure = Superstructure.instance
    private val cheesyDriveHelper = CheesyDriveHelper()
  

   // private val intake = Intake.instance



    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {

            DashboardConfigurator.initDashboard()
//            enabledLooper.register(drive.loop)

      //      enabledLooper.register(intake.loop)
//            enabledLooper.register(superstructure.loop)



            enabledLooper.register(drive.loop)
    //        enabledLooper.register(leds.loop)
            enabledLooper.register(wrist.loop)

            enabledLooper.register(elevator.loop)

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
        try {
            autoModeExecuter?.stop()
            autoModeExecuter = null

            disabledLooper.stop() // end DisabledLooper
            enabledLooper.start() // start EnabledLooper

            autoModeExecuter = AutoModeExecuter()
            //autoModeExecuter?.setAutoMode(HatchPanelOnly(DashboardConfigurator.StartingPosition.LEFT, 0.0))
            autoModeExecuter?.start()
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
            enabledLooper.register(elevator.loop)
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
//            leds.handleFrontDown()
            println("Period")
            if (Math.abs(controlBoard.elevatorPower) > Constants.Elevator.MIN_TRIGGER){
                elevator.setOpenLoop(controlBoard.elevatorPower)
            }
            else{
                elevator.setOpenLoop(0.0)
            }





            if(controlBoard.hatchPExtend){
                   // intake.extended = true
            }
            if(controlBoard.hatchPOut){
                    //intake.hatchOut = true

            }

//            when {
//                controlBoard.elevatorHigh -> elevator.elevatorState = if (elevator.isHatchPanel) Elevator.ElevatorState.HATCHHIGH else Elevator.ElevatorState.PORTHIGH;
//                controlBoard.elevatorMid -> elevator.elevatorState = if (elevator.isHatchPanel) Elevator.ElevatorState.HATCHMID else Elevator.ElevatorState.PORTMID;
//                controlBoard.elevatorLow -> elevator.elevatorState = if (elevator.isHatchPanel) Elevator.ElevatorState.HATCHLOW else Elevator.ElevatorState.PORTLOW;
//            }

            drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))

            //outputAllToSmartDashboard()
            if (drive.highGear && controlBoard.switchToLowGear) {
                drive.highGear = false
                println("Shifting to low gear")
            } else if (!drive.highGear && controlBoard.switchToHighGear) {
                drive.highGear = true
                println("Shifting to high gear")
            }
            if (controlBoard.aimingOn) {
                println("Activating vision")
                println(vision.visionState)
                vision.setState(Vision.VisionState.SEEKING)
                println(vision.visionState)
            }
            if (controlBoard.aimingOff) {
                println("Deactivating vision")
                println(vision.visionState)
                vision.setState(Vision.VisionState.INACTIVE)
                println(vision.visionState)
            }

//            if (vision.visionState != Vision.VisionState.AIMING) {
//                drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
//            } else if (vision.visionState == Vision.VisionState.SEEKING) {
                if (vision.onTarget) {
                    drive.setLeftRightPower(0.3, 0.3)
                } else if (vision.visionState != Vision.VisionState.INACTIVE) {
                    drive.setLeftRightPower(vision.steeringAdjust, -vision.steeringAdjust)
                } else {
                    drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
                }
//            } else {
//                drive.setLeftRightPower(vision.steeringAdjust, - vision.steeringAdjust)
//            }
            wrist.setOpenLoop(controlBoard.wristPower)
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
        //drive.startLiveWindowMode()
    }

    private fun updateLiveWindowTables() {
        //drive.updateLiveWindowTables()
    }

    private fun updateDashboardFeedback() {
        // update things such as "is robot aligned with peg"
    }
}
