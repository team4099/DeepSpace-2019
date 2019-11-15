package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import org.usfirst.frc.team4099.robot.loops.Loop
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.subsystems.Elevator

import kotlin.math.*



/*
Vision Processing for FRC 4099 2019
Written by Rithvik Bhogavilli and Jason Liu

 */

class Vision private constructor(): Subsystem {
    val elevator = Elevator.instance
    var onTarget = false
    var steeringAdjust = 0.0
    var distance = 0.0
    var dHeight = 0.0


    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
    var tx = table.getEntry("tx").getDouble(0.0)
    var tv = table.getEntry("tv").getDouble(0.0)
    var ty = table.getEntry("ty").getDouble(0.0)
    var ta = table.getEntry("ta").getDouble(0.0)

    var led = table.getEntry("ledMode")
    var pipeline = table.getEntry("pipeline")

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
    init {
        led.setNumber(1)
    }
    val loop: Loop = object : Loop {
        override fun onStart() {
            visionState = VisionState.INACTIVE
        }

        override fun onLoop() {
            synchronized(this@Vision) {
//                println("vision loop")
                if(elevator.isHatchPanel){
                    dHeight = Constants.Vision.HATCH_PANEL_HEIGHT - Constants.Vision.TARGET_HEIGHT_ADJUST - elevator.observedElevatorPosition
                }
                else{
                    dHeight = Constants.Vision.CARGO_HEIGHT - Constants.Vision.TARGET_HEIGHT_ADJUST - elevator.observedElevatorPosition
                }

                // distance: d = (h2-h1) / tan(a1+a2)
                distance = (Constants.Vision.TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) / tan(Constants.Vision.CAMERA_ANGLE + ty)
                SmartDashboard.putNumber("vision/distance", distance)
                tx = table.getEntry("tx").getDouble(0.0)
                tv = table.getEntry("tv").getDouble(0.0)
                ty = table.getEntry("ty").getDouble(0.0)
                ta = table.getEntry("ta").getDouble(0.0)

                when (visionState) {
                    VisionState.AIMING -> {
                        pipeline.setNumber(0)
                        led.setNumber(3)
                        if (tv == 0.0) {

                        } else {
                            if (tx > (Constants.Vision.CAMERA_OFFSET)) {
                                // right
                                steeringAdjust = Constants.Vision.Kp * tx - Constants.Vision.minCommand
                            } else if (tx < (Constants.Vision.CAMERA_OFFSET)) {
                                // left
                                steeringAdjust = Constants.Vision.Kp * tx + Constants.Vision.minCommand
                            }
                            else {
                                onTarget = true
                            }
                        }
                        steeringAdjust * 0.8

                        steeringAdjust = -steeringAdjust

                    }
                    VisionState.SEEKING -> {
                        pipeline.setNumber(0)
                        led.setNumber(3)
                        if (tv == 0.0) {

                        } else {
                            if (tx > 0.0) {
                                // right
                                steeringAdjust = Constants.Vision.Kp * tx - Constants.Vision.minCommand
                            } else if (tx < 0.0) {
                                // left
                                steeringAdjust = Constants.Vision.Kp * tx + Constants.Vision.minCommand
                            } else if (tx == 1.0) {
                                onTarget = ta < 0.8
                            } else {}
                        }
                    }

                    VisionState.INACTIVE -> {
                        steeringAdjust = 0.0
                        pipeline.setNumber(1)
                        led.setNumber(1)
                    }

                }
            }
        }
        override fun onStop() {}

    }

    fun setState (state: VisionState) {
        visionState = state
    }
//    fun getDistance(): Double {
//        return distance
//    }

    companion object {
        val instance = Vision()
    }
    override fun stop() {}
    override fun zeroSensors() { }
}
