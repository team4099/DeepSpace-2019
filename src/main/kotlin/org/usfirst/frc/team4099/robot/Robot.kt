package org.usfirst.frc.team4099.robot

import org.usfirst.frc.team4099.lib.util.Utils


import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.auto.modes.HatchPanelOnly
import edu.wpi.first.wpilibj.TimedRobot
//import org.usfirst.frc.team4099.auto.AutoModeExecuter
import org.usfirst.frc.team4099.lib.util.CrashTracker

import org.usfirst.frc.team4099.robot.drive.CheesyDriveHelper
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Looper
import org.usfirst.frc.team4099.robot.loops.VoltageEstimator

import org.usfirst.frc.team4099.robot.subsystems.*
import src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems.Superstructure

class Robot : TimedRobot() {
    private val vision = Vision.instance

    private var autoModeExecuter: AutoModeExecuter? = null


//    private val test3 : DoubleSolenoid = DoubleSolenoid(1,6)
    //private val climber = Climber.instance

    private val wrist = Wrist.instance
    private val intake = Intake.instance

    private val drive = Drive.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
  //  private val leds = LED.instance
    private val elevator = Elevator.instance
    private val superstructure = Superstructure.instance
    private val cheesyDriveHelper = CheesyDriveHelper()

    private var intakeState = IntakeState.HATCHPANEL

  
    public enum class IntakeState {
        CARGO, HATCHPANEL
    }
   // private val intake = Intake.instance



    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {

            DashboardConfigurator.initDashboard()
//            enabledLooper.register(drive.loop)

            enabledLooper.register(intake.loop)
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
            autoModeExecuter?.setAutoMode(HatchPanelOnly(DashboardConfigurator.StartingPosition.LEFT, 0.0))
            autoModeExecuter?.start()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("autonomousInit", t)
            throw t
        }

    }

    override fun teleopInit() {
        try {
//            enabledLooper.register(superstructure.loop)
//            enabledLooper.register(drive.loop)
//            enabledLooper.register(vision.loop)
//            enabledLooper.register(elevator.loop)
//            enabledLooper.register(intake.loop)
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

            if (controlBoard.cargoMode){
                intakeState = IntakeState.CARGO
                intake.hatchState = Intake.HatchState.CLOSED
            }
            else if (controlBoard.hatchPanelMode) {
                intakeState = IntakeState.HATCHPANEL
                wrist.wristState = Wrist.WristState.HORIZONTAL
                elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
            }

//            if(controlBoard.runIntake){
//                intake.intakeState = Intake.IntakeState.IN
//            }
//            else{
//                intake.intakeState = Intake.IntakeState.STOP
//            }
//            if(controlBoard.reverseIntakeFast){
//                intake.intakeState = Intake.IntakeState.FAST_OUT
//            }
//            else if(intake.intakeState == Intake.IntakeState.FAST_OUT){
//                intake.intakeState = Intake.IntakeState.STOP
//            }
            if (intakeState == IntakeState.CARGO){
                if (Math.abs(controlBoard.wristPower)> 0.2) {
                    wrist.setWristVelocity(-controlBoard.wristPower * Constants.Wrist.MAX_SPEED)
                    //wrist.setOpenLoop(-controlBoard.wristPower)
                }
                else {
                    wrist.setWristVelocity(0.0)
                    //wrist.setOpenLoop(0.0)
                }
                if (controlBoard.runCargoIntake){
                    intake.intakeState = Intake.IntakeState.IN
                }
                else if (controlBoard.reverseCargoIntake){
                    intake.intakeState = Intake.IntakeState.OUT
                }
                else if (controlBoard.holdCargo) {
                    intake.intakeState = Intake.IntakeState.HOLDING
                }
                else {
                    intake.intakeState = Intake.IntakeState.STOP
                }

            }
            else {
                if(controlBoard.openHatch){
                    intake.hatchState = Intake.HatchState.OPEN
                }
                if(controlBoard.closeHatch){
                    intake.hatchState = Intake.HatchState.CLOSED
                }
            }
//            if (controlBoard.hatchPExtend) {
//                intake.hatchState = Intake.HatchState.OUT
//            }
//            if (controlBoard.hatchPDextend) {
//                intake.hatchState = Intake.HatchState.IN
//            }
//            if (controlBoard.hatchPOut) {
//                intake.extended = true
//            } else {
//                intake.extended = false
//            }

//            elevator.elevatorState = when{
//                controlBoard.elevatorHigh -> if (elevator.isHatchPanel) Elevator.ElevatorState.HATCHHIGH else Elevator.ElevatorState.PORTHIGH;
//                controlBoard.elevatorMid -> if (elevator.isHatchPanel) Elevator.ElevatorState.HATCHMID else Elevator.ElevatorState.PORTMID;
//                controlBoard.elevatorLow -> if (elevator.isHatchPanel) Elevator.ElevatorState.HATCHLOW else Elevator.ElevatorState.PORTLOW;
//                else -> elevator.elevatorState
//            }

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
//            if(controlBoard.toggleWrist){
//                if(wrist.wristState == Wrist.WristState.VERTICAL){
//                    wrist.wristState = Wrist.WristState.HORIZONTAL
//                }
//                else /*if (wrist.wristState == Wrist.WristState.HORIZONTAL)*/{
//                    wrist.wristState = Wrist.WristState.VERTICAL
//                }
//            }
//
//            if (Math.abs(controlBoard.wristPower)> 0.2) {
//                wrist.setWristVelocity(-controlBoard.wristPower * Constants.Wrist.MAX_SPEED)
//                //wrist.setOpenLoop(-controlBoard.wristPower)
//            }
//            else {
//                wrist.setWristVelocity(0.0)
//                //wrist.setOpenLoop(0.0)
//            }
            //wrist.setWristMode(Wrist.WristState.HORIZONTAL)
            //elevator.elevatorState = Elevator.ElevatorState.HATCHHIGH
            outputAllToSmartDashboard()
            //elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
            if (controlBoard.elevatorLow){
                if(intakeState == IntakeState.HATCHPANEL) {
                    elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
                }
                else {
                    elevator.elevatorState = Elevator.ElevatorState.PORTLOW
                }
            }
            if (controlBoard.elevatorMid){
                if(intakeState == IntakeState.HATCHPANEL) {
                    elevator.elevatorState = Elevator.ElevatorState.HATCHMID
                }
                else {
                    elevator.elevatorState = Elevator.ElevatorState.PORTMID
                }
            }
            if (controlBoard.elevatorHigh){
                if(intakeState == IntakeState.HATCHPANEL) {
                    elevator.elevatorState = Elevator.ElevatorState.HATCHHIGH
                }
                else {
                    elevator.elevatorState = Elevator.ElevatorState.PORTHIGH
                }
            }
            if (Math.abs(controlBoard.elevatorPower) > Constants.Elevator.MIN_TRIGGER) {
                elevator.setElevatorVelocity(1000.0 * controlBoard.elevatorPower)
            }
            else{
                elevator.setElevatorVelocity(0.0)
                elevator.setOpenLoop(0.0)
            }

            //elevator.setElevatorVelocity(1000.0 * controlBoard.elevatorPower)
            //elevator.setOpenLoop(controlBoard.elevatorPower)
           // wrist.setOpenLoop(controlBoard.wristPower)
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
