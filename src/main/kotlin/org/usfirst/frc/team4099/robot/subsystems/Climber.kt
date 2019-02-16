//package org.usfirst.frc.team4099.robot.subsystems
//
//import edu.wpi.first.wpilibj.DoubleSolenoid
//import com.ctre.phoenix.motorcontrol.ControlMode
//import edu.wpi.first.wpilibj.Spark
//import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
//import com.revrobotics.CANSparkMax
//import org.usfirst.frc.team4099.robot.Constants
//import org.usfirst.frc.team4099.robot.loops.Loop
//
//
//class Climber private constructor() : Subsystem {
//    private val motor1: CANSparkMax = CANSparkMax(deviceID1, MotorType.kBrushless)
//    private val motor2: CANSparkMax = CANSparkMax(deviceID2, MotorType.kBrushless)
//
//    enum class ClimberState {
//        FRONT_DOWN, BACK_DOWN, BOTH_UP
//    }
//    var climberState = ClimberState.BOTH_UP
//    override fun outputToSmartDashboard() {
//        SmartDashboard.putString("climber/climberState", climberState.toString())
//    }
//
//    override fun stop() {
//
//    }
//
//    override fun zeroSensors() {
//    }
//
//    val loop: Loop = object : Loop {
//        override fun onStart() {
//            climberState = ClimberState.BOTH_UP
//
//        }
//        override fun onLoop() {
//            synchronized(this@Climber) {
//                when(climberState) {
////                    ClimberState.FRONT_DOWN -> {
////                        pneumaticPiston_F1.set(DoubleSolenoid.Value.kForward)
////                    }
////                    ClimberState.BACK_DOWN -> {
////                        pneumaticPiston_B1.set(DoubleSolenoid.Value.kForward)
////                    }
////                    ClimberState.BOTH_UP -> {
////                        pneumaticPiston_F1.set(DoubleSolenoid.Value.kReverse)
////                        pneumaticPiston_B1.set(DoubleSolenoid.Value.kReverse)
////                    }
//
//                }
//            }
//
//
//        }
//        override fun onStop() {
//            climberState = ClimberState.BOTH_UP
//
//        }
//    }
//    companion object {
//        val instance = Climber()
//
//    }
//}