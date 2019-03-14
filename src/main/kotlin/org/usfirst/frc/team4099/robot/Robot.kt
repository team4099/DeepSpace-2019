package org.usfirst.frc.team4099.robot

import edu.wpi.first.cameraserver.CameraServer
import org.usfirst.frc.team4099.lib.util.Utils


import org.usfirst.frc.team4099.DashboardConfigurator
import org.usfirst.frc.team4099.auto.AutoModeExecuter
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
//    private val vision = Vision.instance

    private var autoModeExecuter: AutoModeExecuter? = null


//    private val test3 : DoubleSolenoid = DoubleSolenoid(1,6)

    private val wrist = Wrist.instance
    private val intake = Intake.instance
    private val climber  = Climber.instance
    private val drive = Drive.instance
    private val controlBoard = ControlBoard.instance
    private val disabledLooper = Looper("disabledLooper")
    private val enabledLooper = Looper("enabledLooper")
  //  private val leds = LED.instance
    private val elevator = Elevator.instance
    private val superstructure = Superstructure.instance
    private val cheesyDriveHelper = CheesyDriveHelper()

    private var intakeState = IntakeState.HATCHPANEL
    var dashBoardTest = 0

  
    public enum class IntakeState {
        CARGO, HATCHPANEL
    }
   // private val intake = Intake.instance



    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CameraServer.getInstance().startAutomaticCapture();

            DashboardConfigurator.initDashboard()
//            enabledLooper.register(drive.loop)

            enabledLooper.register(intake.loop)
//            enabledLooper.register(superstructure.loop)

            enabledLooper.register(climber.loop)

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
//        try {
//            autoModeExecuter?.stop()
//            autoModeExecuter = null
//
//            disabledLooper.stop() // end DisabledLooper
//            enabledLooper.start() // start EnabledLooper
//
//            autoModeExecuter = AutoModeExecuter()
//            autoModeExecuter?.setAutoMode(HatchPanelOnly(DashboardConfigurator.StartingPosition.LEFT, 0.0))
//            autoModeExecuter?.start()
//        } catch (t: Throwable) {
//            CrashTracker.logThrowableCrash("autonomousInit", t)
//            throw t
//        }
        teleopInit()
        intake.hatchState = Intake.HatchState.CLOSED

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
            //SmartDashboard.putNumber("Dashboard Test", dashBoardTest * 1.0)
            dashBoardTest++
            outputAllToSmartDashboard()
//            wrist.outputToSmartDashboard()

        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("disabledPeriodic", t)
            throw t
        }

    }

    override fun autonomousPeriodic() {
//        try {
//            outputAllToSmartDashboard()
//            updateDashboardFeedback()
//        } catch (t: Throwable) {
//            CrashTracker.logThrowableCrash("autonomousPeriodic", t)
//            throw t
//        }
        teleopPeriodic()

    }

    override fun teleopPeriodic() {
        try {
//            leds.handleFrontDown()
            println("Period")
//            SmartDashboard.putNumber("Dashboard Test", dashBoardTest * 1.0)
            dashBoardTest++
            if (controlBoard.cargoMode){
                intakeState = IntakeState.CARGO
                intake.hatchState = Intake.HatchState.CLOSED
                intake.deployState = Intake.DeployState.IN
            }
            else if (controlBoard.hatchPanelMode) {
                intakeState = IntakeState.HATCHPANEL
                intake.deployState = Intake.DeployState.OUT
                //elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
            }

            if (intakeState == IntakeState.CARGO){
                intake.deployState = Intake.DeployState.IN
                if (Math.abs(controlBoard.wristPower)> 0.2) {
                    wrist.setWristVelocity(-controlBoard.wristPower * Constants.Wrist.MAX_SPEED)
                    //println("Set wrist velocity")
                    //wrist.setOpenLoop(-controlBoard.wristPower)
                }
                else {
                    wrist.setWristVelocity(0.0)
                    //wrist.setOpenLoop(0.0)
                }
                if (controlBoard.runCargoIntake){
                    intake.intakeState = Intake.IntakeState.IN
                    //println("intake in")
                }
                else if (controlBoard.reverseCargoIntake){
                    intake.intakeState = Intake.IntakeState.OUT
                    //println("intake out")
                }
                else if (controlBoard.holdCargo) {
                    intake.intakeState = Intake.IntakeState.HOLDING
                    //println("intake set to holding")
                }
                else {
                    intake.intakeState = Intake.IntakeState.STOP
                }

            }
            else {
                intake.deployState = Intake.DeployState.OUT
                wrist.wristState = Wrist.WristState.HORIZONTAL
                if(controlBoard.openHatch){
                    intake.hatchState = Intake.HatchState.OPEN
                    //println("open hatch")
                }
                if(controlBoard.closeHatch){
                    intake.hatchState = Intake.HatchState.CLOSED
                    //println("close hatch")
                }
                if (controlBoard.openDeployer){
                    intake.deployState = Intake.DeployState.OUT
                }
                if (controlBoard.closeDeployer){
                    intake.deployState = Intake.DeployState.IN

                }
            }
//            if (controlBoard.climberUp){
//                climber.climberState = Climber.ClimberState.UP
//            }
//            else if (controlBoard.climberDown){
//                climber.climberState = Climber.ClimberState.DOWN
//            }
//            else if (controlBoard.climberDrive){
//                climber.climberState = Climber.ClimberState.FORWARD
//            }
//            else if (Math.abs(controlBoard.climberPower) > 0.2){
//                climber.setOpenLoop(controlBoard.climberPower)
//            }
//            else{
//                climber.climberState = Climber.ClimberState.STILL
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
//            if (controlBoard.aimingOn) {
//                println("Activating vision")
//                println(vision.visionState)
//                vision.setState(Vision.VisionState.AIMING)
//                println(vision.visionState)
//            }
//            if (controlBoard.aimingOff) {
//                println("Deactivating vision")
//                println(vision.visionState)
//                vision.setState(Vixsion.VisionState.INACTIVE)
//                println(vision.visionState)
//            }

//            if (vision.visionState != Vision.VisionState.AIMING) {
//                drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
//            } else if (vision.visionState == Vision.VisionState.SEEKING) {
//                if (vision.onTarget) {
//                    drive.setLeftRightPower(0.3, 0.3)
//                } else if (vision.visionState != Vision.VisionState.INACTIVE) {
//                    drive.setLeftRightPower(vision.steeringAdjust, -vision.steeringAdjust)
//                } else {
//                    drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
//                }
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
            if (controlBoard.elevatorLow){
                println("elevator low")
                if(intakeState == IntakeState.HATCHPANEL) {
                    elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
                }
                else {
                    elevator.elevatorState = Elevator.ElevatorState.PORTLOW
                }
            }
            if (controlBoard.elevatorMid){
                println("elevator mid")
                if(intakeState == IntakeState.HATCHPANEL) {
                    elevator.elevatorState = Elevator.ElevatorState.HATCHMID
                }
                else {
                    elevator.elevatorState = Elevator.ElevatorState.PORTMID
                }
            }
            if (controlBoard.elevatorHigh){
                println("elevator high")
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
            else if(elevator.elevatorState == Elevator.ElevatorState.VELOCITY_CONTROL){
                elevator.setElevatorVelocity(0.0)
            }

            if (controlBoard.elevatorClimberLatch) {
                elevator.elevatorState = Elevator.ElevatorState.CLIMBING
            }

            if (controlBoard.stowClimber){
                println("stow climber")
                climber.climberState = Climber.ClimberState.STOW
            }
            else if (controlBoard.climbToTwo) {
                println("climb to two")
                climber.climberState = Climber.ClimberState.LEVEL_TWO
            }
            else if (controlBoard.climbToTwoHalf){
                println("climb to two half")
                climber.climberState = Climber.ClimberState.LEVEL_TWO_HALF
            }
            else if (controlBoard.climbToThree){
                println("climb to three")
                climber.climberState = Climber.ClimberState.LEVEL_THREE
            }
            else if (controlBoard.climbVeloUp){
                println("climb velocity up")
                climber.climberState = Climber.ClimberState.VELOCITY_CONTROL
                climber.setClimberVelocity(Constants.Climber.MAX_CLIMB_VEL)
            }
            else if (controlBoard.climbVeloDown){
                println("climb velocity down")
                climber.climberState = Climber.ClimberState.VELOCITY_CONTROL
                climber.setClimberVelocity(-Constants.Climber.MAX_CLIMB_VEL)
            }
            else if (climber.climberState == Climber.ClimberState.VELOCITY_CONTROL){
                climber.setClimberVelocity(0.0)
            }
            else{
                climber.setOpenLoop(0.0)
            }
            if (climber.climberState != Climber.ClimberState.STOW){
                if(controlBoard.climberDrive != 0.0){
                    println("climber drive")
                }
                climber.setOpenDrive(Constants.Climber.MAX_DRIVE_VEL * controlBoard.climberDrive)
            }
            climber.setOpenDrive(controlBoard.throttle)
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

        println("Running Intake")
        intake.intakeState = Intake.IntakeState.IN
        Thread.sleep(2000)

        println("Intake Off")
        intake.intakeState = Intake.IntakeState.STOP
        Thread.sleep(3000)

        println("Holding")
        intake.intakeState = Intake.IntakeState.HOLDING
        Thread.sleep(2000)
        println("Intake Off")
        intake.intakeState = Intake.IntakeState.STOP

        println("Hatch Open")
        intake.hatchState = Intake.HatchState.OPEN
        Thread.sleep(3000)

        println("Hatch Close")
        intake.hatchState = Intake.HatchState.CLOSED
        Thread.sleep(3000)

        println("Wrist horizontal")
        wrist.wristState = Wrist.WristState.HORIZONTAL
        Thread.sleep(3000)

//            println("Elevator Hatch low")
//            elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
//            TimeUnit.SECONDS.sleep(3)
//
//            println("Elevator Hatch mid")
//            elevator.elevatorState = Elevator.ElevatorState.HATCHMID
//            TimeUnit.SECONDS.sleep(3)
//
//            println("Elevator Hatch high")
//            elevator.elevatorState = Elevator.ElevatorState.HATCHHIGH
//            TimeUnit.SECONDS.sleep(3)
//
//            println("Elevator Port Low")
//            elevator.elevatorState = Elevator.ElevatorState.PORTLOW
//            TimeUnit.SECONDS.sleep(3)
//
//            println("Elevator port mid")
//            elevator.elevatorState = Elevator.ElevatorState.PORTMID
//            TimeUnit.SECONDS.sleep(3)
//
//            println("Elevator port high")
//            elevator.elevatorState = Elevator.ElevatorState.PORTHIGH
//            TimeUnit.SECONDS.sleep(3)
//
//            println("Elevator ground")
//            elevator.elevatorState = Elevator.ElevatorState.GROUND
//            TimeUnit.SECONDS.sleep(3)

//            println("DT Forward")
//            drive.setLeftRightPower(0.2,0.2)
//            TimeUnit.MILLISECONDS.sleep(500)
//
//            println("DT Backward")
//            drive.setLeftRightPower(-0.2,-0.2)
//            TimeUnit.MILLISECONDS.sleep(500)

        println("Shift DT Gear low")
        drive.highGear = false
        Thread.sleep(3000)


        println("Shift DT Gear high")
        drive.highGear = true
        Thread.sleep(3000)
       // outputAllToSmartDashboard()
    }
    /**
     * Log information from all subsystems onto the SmartDashboard
     */
    private fun outputAllToSmartDashboard() {
        wrist.outputToSmartDashboard()
        intake.outputToSmartDashboard()
        elevator.outputToSmartDashboard()
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
