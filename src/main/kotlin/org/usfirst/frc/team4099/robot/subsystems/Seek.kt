package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import org.usfirst.frc.team4099.robot.loops.Loop

class Seek private constructor(): Subsystem {
    var Kp = -0.1
    var min_command = 0.05
    var steering_adjust = 0.0
    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")

    var tx = table.getEntry("tx").getDouble(0.0)
    var tv = table.getEntry("tv").getDouble(0.0)

    var seekState = SeekState.INACTIVE

    enum class SeekState {
        SEEKING, INACTIVE
    }

    override fun outputToSmartDashboard() {
        SmartDashboard.putNumber("LimelightX", tx);
        SmartDashboard.putNumber("LimelightTarget", tv)
    }

    @Synchronized override fun stop() {
        seekState = SeekState.INACTIVE
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            seekState = SeekState.INACTIVE
        }

        override fun onLoop() {
            synchronized(this@Seek) {
                when (seekState) {
                    SeekState.SEEKING -> {
                        if (tv == 0.0) {
                            steering_adjust = 0.3
                        } else {
                            val heading_error = tx
                            steering_adjust = Kp * tx
                        }

                    }
                    SeekState.INACTIVE -> steering_adjust = 0.0
                }
            }
        }
        override fun onStop() = stop()

    }

    companion object {
        val instance = Seek()
    }

    override fun zeroSensors() { }

}