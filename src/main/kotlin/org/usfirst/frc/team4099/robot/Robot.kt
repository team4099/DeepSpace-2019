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
import src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems.Superstructure

class Robot : TimedRobot() {
   private val vision = Vision.instance

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
    var isDeploy = false

  
    enum class IntakeState {
        CARGO, HATCHPANEL
    }
   // private val intake = Intake.instance



    init {
        CrashTracker.logRobotConstruction()
    }


    override fun robotInit() {
        try {
            CameraServer.getInstance().startAutomaticCapture()

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
            enabledLooper.register(vision.loop)


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
//        intake.hatchState = Intake.HatchState.OPEN
//        try {
//            autoModeExecuter?.stop()
//            autoModeExecuter = null
//
//            disabledLooper.stop() // end DisabledLooper
//            enabledLooper.start() // start EnabledLooper
//
//            autoModeExecuter = AutoModeExecuter()
//            autoModeExecuter?.setAutoMode(DashboardConfigurator.getAutonomousMode())
//            autoModeExecuter?.start()
//        } catch (t: Throwable) {
//            CrashTracker.logThrowableCrash("autonomousInit", t)
//            throw t
//        }
        teleopInit()
        wrist.wristState = Wrist.WristState.HORIZONTAL

    }

    override fun teleopInit() {
        try {
//            enabledLooper.register(superstructure.loop)
//            enabledLooper.register(drive.loop)
//            enabledLooper.register(vision.loop)
//            enabledLooper.register(elevator.loop)
//            enabledLooper.register(intake.loop)
            if (DashboardConfigurator.getIntakeMode() == "Cargo") {
                intakeState = IntakeState.CARGO
            }
            enabledLooper.start()
        } catch (t: Throwable) {
            CrashTracker.logThrowableCrash("teleopInit", t)
            throw t
        }
        wrist.wristState = Wrist.WristState.HORIZONTAL

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
                wrist.wristState = Wrist.WristState.HORIZONTAL
               // intake.deployState = Intake.DeployState.OUT
                //elevator.elevatorState = Elevator.ElevatorState.HATCHLOW
            }

            if (intakeState == IntakeState.CARGO){
                intake.deployState = Intake.DeployState.IN
                if (Math.abs(controlBoard.wristPower)> 0.1) {
                    wrist.setWristVelocity(-controlBoard.wristPower * Constants.Wrist.MAX_SPEED)
                    wrist.setWristVelocity(-controlBoard.wristPower * Constants.Wrist.MAX_SPEED)
                    //println("Set wrist velocity")
                    //wrist.setOpenLoop(-controlBoard.wristPower)
                }
                else if (wrist.wristState == Wrist.WristState.VELOCITY_CONTROL) {
                    wrist.setWristVelocity(0.0)
                    //wrist.setOpenLoop(0.0)
                }
                if(controlBoard.wristCargoIntake){
                    wrist.wristState = Wrist.WristState.CARGO
                }
                if (controlBoard.runCargoIntake){
                    intake.intakeState = Intake.IntakeState.IN
                    //println("intake in")
                }
                else if (controlBoard.reverseCargoIntake){
                    intake.intakeState = Intake.IntakeState.OUT
                    //println("intake out")
                }
//                else if (controlBoard.holdCargo) {
//                    intake.intakeState = Intake.IntakeState.HOLDING
//                    //println("intake set to holding")

                else {
                    intake.intakeState = Intake.IntakeState.HOLDING
                }

            }
            else {
                //intake.deployState = Intake.DeployState.OUT
                intake.intakeState = Intake.IntakeState.STOP
                //wrist.wristState = Wrist.WristState.HORIZONTAL
                if (Math.abs(controlBoard.wristPower)> 0.07) {
                    wrist.setWristVelocity(-controlBoard.wristPower * Constants.Wrist.MAX_SPEED/3)
                    //println("Set wrist velocity")
                    //wrist.setOpenLoop(-controlBoard.wristPower)
                }
                else if (wrist.wristState == Wrist.WristState.VELOCITY_CONTROL) {
                    wrist.setWristVelocity(0.0)
                    //wrist.setOpenLoop(0.0)
                }
                if(controlBoard.wristCargoIntake){
                    wrist.wristState = Wrist.WristState.HORIZONTAL
                }
                if(controlBoard.openHatch){
                    isDeploy = true
                    intake.deployState = Intake.DeployState.OUT

                    //intake.hatchState = Intake.HatchState.OPEN
                    //println("open hatch")
                }
                else {
                    if (isDeploy) {
                        intake.hatchState = Intake.HatchState.OPEN
                        intake.deployState = Intake.DeployState.IN
                    }
                    isDeploy = false
                }
                if(controlBoard.closeHatch){
                    intake.hatchState = Intake.HatchState.CLOSED
                    //println("close hatch")
                }

            }

            //drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(0.5 * controlBoard.throttle, 0.7* controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
            val driveSig = cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1))
            drive.setVelocitySetpoint(6.0 * controlBoard.throttle + controlBoard.turn, 6.0 * controlBoard.throttle - controlBoard.turn, 0.0, 0.0)

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
                vision.setState(Vision.VisionState.AIMING)
                println(vision.visionState)
            }
            else {
                println("Deactivating vision")
                println(vision.visionState)
                vision.setState(Vision.VisionState.INACTIVE)
                println(vision.visionState)
            }

            if (vision.visionState != Vision.VisionState.AIMING) {
                drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
            } else if (vision.visionState == Vision.VisionState.AIMING) {
                if (vision.visionState != Vision.VisionState.INACTIVE) {
                    drive.setLeftRightPower(vision.steeringAdjust, -vision.steeringAdjust)
                } else {
                    drive.setOpenLoop(cheesyDriveHelper.curvatureDrive(controlBoard.throttle, controlBoard.turn, Utils.around(controlBoard.throttle, 0.0, 0.1)))
                }
            } else {
//                drive.setLeftRightPower(vision.steeringAdjust, - vision.steeringAdjust)
            }

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
            println("requested elevator power: ${controlBoard.elevatorPower}")
            if (Math.abs(controlBoard.elevatorPower) > Constants.Elevator.MIN_TRIGGER) {
                elevator.setElevatorVelocity(500.0 * controlBoard.elevatorPower)
            }
            else if(elevator.elevatorState == Elevator.ElevatorState.VELOCITY_CONTROL){
                elevator.setElevatorVelocity(0.0)
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
//                climber.setOpenLoop(0.5)
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

            if(controlBoard.feetRetract) {
                climber.feet = Climber.FeetState.RETRACT
            }
//            else if (controlBoard.feetExtend) {
//                climber.feet = Climber.FeetState.EXTEND
//            }
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
        climber.outputToSmartDashboard()
    }
    /**
     * Log information from all subsystems onto the SmartDashboard
     */
    private fun outputAllToSmartDashboard() {
        wrist.outputToSmartDashboard()
        intake.outputToSmartDashboard()
        elevator.outputToSmartDashboard()
        drive.outputToSmartDashboard()
        climber.outputToSmartDashboard()
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
