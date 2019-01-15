package src.main.kotlin.org.usfirst.frc.team4099.robot.subsystems

import org.usfirst.frc.team4099.robot.subsystems.Drive
import org.usfirst.frc.team4099.robot.subsystems.Intake

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
    private val mDrive = Drive.instance

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

    private val mSystemState = SystemState.IDLE
    private val mWantedState = WantedState.IDLE

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
           when(mSystemState) {
               IDLE -> handleIdle()
               ALIGNING_LINE, ALIGNING_VISION -> handleVision()
               INTAKE_UP -> handleElevatorUp()
               CLIMBING -> handleClimb()
               INTAKE_CARGO -> handleCargoIntake()
               UNJAMMING -> handleUnjam()
               BLINK -> handleBlink()
            }
        }
    }

    private fun handleIdle() {
        // TODO
    }

    private fun handleVision() {
        // TODO
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