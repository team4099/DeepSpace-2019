package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Loop

class Vision private constructor(): Subsystem {
    var Kp = -0.1
    var min_command = 0.05
    var steering_adjust = 0.0
    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
    var tx = table.getEntry("tx").getDouble(0.0)

    var visionState = VisionState.INACTIVE

    enum class VisionState {
        AIMING, INACTIVE
    }

    override fun outputToSmartDashboard() {
        SmartDashboard.putNumber("LimelightX", tx);
    }

    @Synchronized override fun stop() {
        visionState = VisionState.INACTIVE
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            visionState = VisionState.INACTIVE
        }

        override fun onLoop() {
            synchronized(this@Vision) {
                when (visionState) {
                    VisionState.AIMING -> {
                        val heading_error = (tx * -1.0)
                        if (tx > 1.0) {
                            steering_adjust = Kp * heading_error - min_command
                        } else if (tx < 1.0) {
                            steering_adjust = Kp * heading_error + min_command
                        }

                    }
                    VisionState.INACTIVE -> steering_adjust = 0.0
                }
            }
        }
        override fun onStop() = stop()

    }

    companion object {
        val instance = Vision()
    }

    override fun zeroSensors() { }
}
