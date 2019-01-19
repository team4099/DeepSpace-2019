package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.usfirst.frc.team4099.robot.loops.Loop

class Aim private constructor(): Subsystem {
    var Kp = -0.1
    var min_command = 0.05
    var steering_adjust = 0.0

    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
    var tx = table.getEntry("tx").getDouble(0.0)
    val heading_error = (tx * -1.0)

    var aimState = AimState.INACTIVE

    enum class AimState {
        AIMING, INACTIVE
    }

    override fun outputToSmartDashboard() {
        SmartDashboard.putNumber("LimelightX", tx);
    }

    @Synchronized override fun stop() {
        aimState = AimState.INACTIVE
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            aimState = AimState.INACTIVE
        }

        override fun onLoop() {
            synchronized(this@Aim) {
                when (aimState) {
                    AimState.AIMING -> {
                        if (tx > 1.0) {
                            steering_adjust = Kp * heading_error - min_command
                        } else if (tx < 1.0) {
                            steering_adjust = Kp * heading_error + min_command
                        }

                    }
                    AimState.INACTIVE -> steering_adjust = 0.0
                }
            }
        }
        override fun onStop() = stop()

    }

    companion object {
        val instance = Aim()
    }

    override fun zeroSensors() { }
}
