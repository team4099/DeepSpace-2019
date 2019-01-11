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
    const val intake = Intake.instance
    const val mDrive = Drive.instance

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
            val newState = mSystemState
            newState = when(mSystemState) {
                IDLE -> handleIdle()
                INTAKE_UP -> handleElevatorUp()
            }
        }
    }

    private fun handleElevatorUp() {
        intake.up = false
        // Move elevator up
    }
}