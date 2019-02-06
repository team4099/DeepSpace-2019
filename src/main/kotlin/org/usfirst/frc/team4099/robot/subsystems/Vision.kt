package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import org.usfirst.frc.team4099.robot.loops.Loop
import org.usfirst.frc.team4099.robot.Constants

class Vision private constructor(): Subsystem {

    var steeringAdjust = 0.0
    var distance = 0

    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
    var tx = table.getEntry("tx").getDouble(0.0)
    var tv = table.getEntry("tv").getDouble(0.0)
    var ty = table.getEntry("ty").getDouble(0.0)

    var visionState = VisionState.INACTIVE


    enum class VisionState {
        AIMING, INACTIVE, SEEKING
    }

    override fun outputToSmartDashboard() {
        SmartDashboard.putNumber("LimelightX: ", tx)
        SmartDashboard.putNumber("LimelightTarget: ", tv)
        SmartDashboard.putNumber("LimelightY: ", ty)
    }
//
//    @Synchronized override fun stop() {
//        visionState = VisionState.INACTIVE
//    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            visionState = VisionState.INACTIVE
        }

        override fun onLoop() {
            synchronized(this@Vision) {
//                println("vision loop")
                distance = (Math.tan(ty + Constants.Vision.CAMERA_ANGLE) / Constants.Vision.CAMERA_TO_TARGET_HEIGHT).toInt()
                tx = table.getEntry("tx").getDouble(0.0)
                tv = table.getEntry("tv").getDouble(0.0)
                ty = table.getEntry("ty").getDouble(0.0)
                when (visionState) {
                    VisionState.AIMING -> {
                        if (tx > 1.0) {
                            steeringAdjust = Constants.Vision.Kp * tx - Constants.Vision.minCommand
                        } else if (tx < 1.0) {
                            steeringAdjust = Constants.Vision.Kp * tx + Constants.Vision.minCommand
                        }
                        steeringAdjust = -steeringAdjust

                    }
                    VisionState.SEEKING -> {
                        if (tv == 0.0) {
                            steeringAdjust = 0.3
                        } else {
                            steeringAdjust = Constants.Vision.Kp * tx
                        }
                    }

                    VisionState.INACTIVE -> steeringAdjust = 0.0

                }
            }
        }
        override fun onStop() {}

    }

    companion object {
        val instance = Vision()
    }
    override fun stop() {}
    override fun zeroSensors() { }
}
