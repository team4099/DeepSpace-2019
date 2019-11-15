package src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems

import org.usfirst.frc.team4099.robot.loops.Loop
import org.usfirst.frc.team4099.robot.subsystems.*

/**
 * @author Team 4099
 *
 * This class is the constructor for the Superstructure
 *
 * @constructor Creates the Superstructure
 *
 */

class Superstructure : Subsystem {
  //  val mInstance = Superstructure()

    // Put Subsystem instantiation here:
    private val intake = Intake.instance
    private val drive = Drive.instance
    private val elevator = Elevator.instance
    private val vision = Vision.instance
    private val climber = Climber.instance
    private val wrist = Wrist.instance
//    private val led = LED.instance
//    private val grabber = Grabber.instance

    //private val climber = Climber.instance

    enum class SystemState {
        IDLE,
        ALIGNING_LINE, // Waiting for alignment with either line or vision target
        ALIGNING_VISION,
        INTAKE_UP, // Intake goes up wow
        CLIMBING, // Robot is currently climbing
        INTAKE_CARGO, // Robot is currently intaking cargo (intake is down)
        UNJAMMING, // Unjamming the rolly grabber intake
        BLINK // Blink to let drivers know the robot is aligned
    }
    // What the user wants to do
    enum class WantedState {
        IDLE, CLIMB, UNJAM, INTAKE_CARGO, ALIGN
    }

    private var systemState = SystemState.IDLE
    private val wantedState = WantedState.IDLE

    private fun isAlignedVision(): Boolean {
        if (vision.tx == 0.0 && vision.tv == 1.0 && vision.ty == 0.0) {
            return true
        }
        return false
    }
    val loop: Loop = object : Loop {
        override fun onStart() {
            systemState = SystemState.IDLE
        }
        override fun onStop(){

        }

        override fun onLoop() {
            synchronized(this@Superstructure) {
                if (elevator.isHatchPanel) {
                    //led.setStateColors("WHITE", LED.SystemState.SOLID)
                } else {
                    //led.setStateColors("ORANGE", LED.SystemState.SOLID)
                }

                when (systemState) {
                    SystemState.IDLE -> handleIdle()
                    SystemState.CLIMBING -> climbUp()
//                    SystemState.ALIGNING_VISION -> handleVision()
//                SystemState.INTAKE_UP -> handleElevatorUp()
//                SystemState.CLIMBING -> handleClimb()
//                SystemState.INTAKE_CARGO -> handleCargoIntake()
//                SystemState.UNJAMMING -> handleUnjam()
//                SystemState.BLINK -> handleBlink()
                }
            }
        }
    }

    private fun handleIdle() {
        // TODO
//        vision.visionState = Vision.VisionState.INACTIVE
//        elevator.elevatorState = Elevator.ElevatorState.PORTLOW
        //led.setState(LED.SystemState.OFF)
    }

    private fun handleVision() {
        vision.visionState = Vision.VisionState.AIMING
        println("vision handled")

        //led.setStateColors("PURPLE", LED.SystemState.SOLID)
    }

//    private fun handleElevatorUp() {
//        intake.up = false // Move intake down
//         // Move elevator up
//        elevator.updatePosition(true)
//    }
//
//    private fun handleClimb() {
//        when(climber.climberState){
//            Climber.ClimberState.FRONT_DOWN -> led.setState(LED.SystemState.FRONT_DOWN)
//            Climber.ClimberState.BACK_DOWN -> led.setState(LED.SystemState.BACK_DOWN)
//        }
//    }
//
//    private fun handleCargoIntake() {
//        when(intake.intakeState) {
//            Intake.IntakeState.IN, Intake.IntakeState.SLOW -> led.setState(LED.SystemState.INTAKE_IN)
//        }
//    }
//
//    private fun handleUnjam() {
//        if (intake.intakeState == Intake.IntakeState.FAST_OUT && grabber.intakeState == Grabber.IntakeState.OUT) {
//            led.setState(LED.SystemState.UNJAM)
//        }
//    }
//
//    private fun handleBlink() {
//        if (isAlignedVision()) {
//            led.setState(LED.SystemState.ALIGNING)
//        }
//    }

    fun setState (state: SystemState) {
        systemState = state
    }

    private fun climbUp() {
        elevator.elevatorState = Elevator.ElevatorState.CLIMBING
        wrist.setWristPosition(-25.74*1.3)//1.3 is subject to change, -25.74 is horizontal encoder val
        climber.climberState = Climber.ClimberState.LEVEL_THREE
    }

    private fun climbOn() {
        drive.setLeftRightPower(0.1,0.1)
        climber.setOpenDrive(0.1)
    }

    private fun stowClimber() {
        climber.climberState = Climber.ClimberState.STOW
        drive.setLeftRightPower(0.1,0.1)
    }

    override fun outputToSmartDashboard() { }

    override fun stop() { }

    override fun zeroSensors() { }
    companion object {
        val instance = Superstructure()
    }

}
