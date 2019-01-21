package src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems

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
    val mInstance = Superstructure()

    // Put Subsystem instantiation here:
    private val intake = Intake.instance
    private val drive = Drive.instance
    private val elevator = Elevator.instance
    private val vision = Vision.instance

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

    fun isAlignedLine() {
        // Get information from line follow sensor
        // Add isAligned() to line follow subsystem
    }

    fun isAlignedVision() {
        // Get information from limelight subsystem
        // Add isAligned to limelight subsystem
    }

    fun onLoop() {
        synchronized(this@Superstructure) {
           when(systemState) {
               SystemState.IDLE -> handleIdle()
               SystemState.ALIGNING_LINE, SystemState.ALIGNING_VISION -> handleVision()
               SystemState.INTAKE_UP -> handleElevatorUp()
               SystemState.CLIMBING -> handleClimb()
               SystemState.INTAKE_CARGO -> handleCargoIntake()
               SystemState.UNJAMMING -> handleUnjam()
               SystemState.BLINK -> handleBlink()
            }
        }
    }

    private fun handleIdle() {
        // TODO
    }

    private fun handleVision() {
        vision.visionState = Vision.VisionState.AIMING
        drive.setLeftRightPower(vision.steeringAdjust, -vision.steeringAdjust)
    }

    private fun handleElevatorUp() {
        intake.up = false // Move intake down
         // Move elevator up
    }

    private fun handleClimb() {
        // TODO
    }

    private fun handleCargoIntake() {
        // TODO
    }

    private fun handleUnjam() {
        // TODO
    }

    private fun handleBlink() {
        // TODO
    }
}