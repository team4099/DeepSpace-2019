package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.lib.util.CANMotorControllerFactory
import org.usfirst.frc.team4099.lib.util.Utils
import org.usfirst.frc.team4099.lib.util.conversions.ElevatorConversion
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.Loop

class Elevator private constructor(): Subsystem {
    private val talon = CANMotorControllerFactory.createDefaultTalon(Constants.Elevator.ELEVATOR_TALON_ID)
    private val slave = CANMotorControllerFactory.createPermanentSlaveVictor(Constants.Elevator.SLAVE_VICTOR_ID, talon)

    private var elevatorPower = 0.0
    var maxElevatorV = 0.0
    var wantedElevatorPower = 0.0
    var elevatorState = ElevatorState.VELOCITY_CONTROL //change now
    var movementState = MovementState.STILL
        private set
    var observedElevatorPosition = 0.0
        private set
    var observedElevatorVelocity = 0.0
        private set
    var isHatchPanel = true
   // private val climberLatch: DoubleSolenoid = DoubleSolenoid(Constants.Elevator.LATCH_FORWARD_ID, Constants.Elevator.LATCH_REVERSE_ID)

    var stopPosition = 0.0

    enum class ElevatorState (val targetPos : Double) {
        GROUND(0.0),
        HATCHLOW(10.0), HATCHMID(133.0), HATCHHIGH(235.0),  //not set
        PORTLOW(28.0), PORTMID(160.0), PORTHIGH(253.0),     //low, mid set
        HAB_THREE(Constants.Elevator.HAB_THREE), HAB_TWO(Constants.Elevator.HAB_TWO),
        HAB_TWO_HALF(Constants.Elevator.HAB_TWO_HALF), CLIMBING(0.0),
        VELOCITY_CONTROL(Double.NaN), OPEN_LOOP(Double.NaN)
    }

    enum class MovementState {
        UP, DOWN, STILL
    }

    init {
        talon.inverted = true
        slave.inverted = true
        //talon.setNeutralMode(NeutralMode.Brake)
        //slave.setNeutralMode(NeutralMode.Brake)
        talon.clearStickyFaults(0)
        talon.setSensorPhase(true)
        talon.configContinuousCurrentLimit(25)
        talon.configPeakCurrentLimit(40)

        //talon.set(ControlMode.MotionMagic, 0.0)
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
        talon.configNominalOutputForward(0.0, 0)
        talon.configNominalOutputReverse(0.0, 0)
        talon.configPeakOutputReverse(-1.0, 0)
        talon.configPeakOutputForward(1.0, 0)
        talon.config_kP(0, Constants.Gains.ELEVATOR_UP_KP, 0)
        talon.config_kI(0, Constants.Gains.ELEVATOR_UP_KI, 0)
        talon.config_kD(0, Constants.Gains.ELEVATOR_UP_KD, 0)
        talon.config_kF(0, Constants.Gains.ELEVATOR_UP_KF, 0)

        talon.config_kP(1, Constants.Gains.ELEVATOR_DOWN_KP, 0)
        talon.config_kI(1, Constants.Gains.ELEVATOR_DOWN_KI, 0)
        talon.config_kD(1, Constants.Gains.ELEVATOR_DOWN_KD, 0)
        talon.config_kF(1, Constants.Gains.ELEVATOR_DOWN_KF, 0)

        talon.config_kP(2, Constants.Gains.ELEVATOR_UP_KP_V, 0)
        talon.config_kI(2, Constants.Gains.ELEVATOR_UP_KI_V, 0)
        talon.config_kD(2, Constants.Gains.ELEVATOR_UP_KD_V, 0)
        talon.config_kF(2, Constants.Gains.ELEVATOR_UP_KF_V, 0)

        talon.config_kP(3, Constants.Gains.ELEVATOR_DOWN_KP_V, 0)
        talon.config_kI(3, Constants.Gains.ELEVATOR_DOWN_KI_V, 0)
        talon.config_kD(3, Constants.Gains.ELEVATOR_DOWN_KD_V, 0)
        talon.config_kF(3, Constants.Gains.ELEVATOR_DOWN_KF_V, 0)

        talon.configMotionCruiseVelocity(1600, 0)
        talon.configMotionAcceleration(925, 0)

        zeroSensors()

        talon.configReverseSoftLimitEnable(true, 0)
        talon.configReverseSoftLimitThreshold(ElevatorConversion.inchesToPulses(Constants.Elevator.BOTTOM_SOFT_LIMIT).toInt(), 0)
        talon.overrideSoftLimitsEnable(true)

        //SmartDashboard.putNumber("elevator/pidPDown", Constants.Gains.ELEVATOR_DOWN_KP)
        //SmartDashboard.putNumber("elevator/pidIDown", Constants.Gains.ELEVATOR_DOWN_KI)
        //SmartDashboard.putNumber("elevator/pidDDown", Constants.Gains.ELEVATOR_DOWN_KD)
        //SmartDashboard.putNumber("elevator/pidFDown", Constants.Gains.ELEVATOR_DOWN_KF)

        //SmartDashboard.putNumber("elevator/pidPUP", Constants.Gains.ELEVATOR_UP_KP)
        //SmartDashboard.putNumber("elevator/pidIUP", Constants.Gains.ELEVATOR_UP_KI)
        //SmartDashboard.putNumber("elevator/pidDUP", Constants.Gains.ELEVATOR_UP_KD)
        //SmartDashboard.putNumber("elevator/pidFUP", Constants.Gains.ELEVATOR_UP_KF)
    }

    fun setOpenLoop(power: Double) {
        elevatorState = ElevatorState.OPEN_LOOP
//        println("Elevator: " + observedElevatorPosition)
        if(observedElevatorPosition < Constants.Elevator.BOTTOM_SOFT_LIMIT  && power < 0.0){ //CHANGE SOFT LIMIT
            talon.set(ControlMode.PercentOutput, power)
        }
        else {
            talon.set(ControlMode.PercentOutput, power)
        }
    }


    fun setElevatorVelocity(inchesPerSecond: Double) {
//        if (inchesPerSecond <= 0.1 && observedElevatorPosition < 5.0) {
//            setOpenLoop(0.0)
//            talon.sensorCollection.setQuadraturePosition(0, 0)
////            println("exiting at 0 power, $inchesPerSecond")
//            return
//        }
        elevatorState = ElevatorState.VELOCITY_CONTROL
        if(inchesPerSecond == 0.0){
            setElevatorPosition(stopPosition)
        } else {
            if (inchesPerSecond > 0) {
                talon.selectProfileSlot(2, 0)
            } else {
                talon.selectProfileSlot(3, 0)
            }
            talon.set(ControlMode.Velocity, inchesPerSecond)
            stopPosition = observedElevatorPosition
        }
        println("nativeVel: $inchesPerSecond, observedVel: ${talon.sensorCollection.quadratureVelocity}")
    }


    override fun outputToSmartDashboard() {
        SmartDashboard.putNumber("elevator/elevatorVoltage", talon.motorOutputVoltage)
        SmartDashboard.putNumber("elevator/elevatorVelocity", talon.sensorCollection.quadratureVelocity.toDouble())
        SmartDashboard.putNumber("elevator/elevatorHeight", observedElevatorPosition)
        SmartDashboard.putNumber("elevator/closedLoopError", talon.getClosedLoopError(0).toDouble())


//        talon.config_kP(1, SmartDashboard.getNumber("elevator/pidPDown", Constants.Gains.ELEVATOR_DOWN_KP), 0)
//        talon.config_kI(1, SmartDashboard.getNumber("elevator/pidIDown", Constants.Gains.ELEVATOR_DOWN_KI), 0)
//        talon.config_kD(1, SmartDashboard.getNumber("elevator/pidDDown", Constants.Gains.ELEVATOR_DOWN_KD), 0)
//        talon.config_kF(1, SmartDashboard.getNumber("elevator/pidFDown", Constants.Gains.ELEVATOR_DOWN_KF), 0)
//
//
//        talon.config_kP(0, SmartDashboard.getNumber("elevator/pidPUP", Constants.Gains.ELEVATOR_UP_KP), 0)
//        talon.config_kI(0, SmartDashboard.getNumber("elevator/pidIUP", Constants.Gains.ELEVATOR_UP_KI), 0)
//        talon.config_kD(0, SmartDashboard.getNumber("elevator/pidDUP", Constants.Gains.ELEVATOR_UP_KD), 0)
//        talon.config_kF(0, SmartDashboard.getNumber("elevator/pidFUP", Constants.Gains.ELEVATOR_UP_KF), 0)

    }


    fun toggleOuttakeMode () {
        isHatchPanel = !isHatchPanel
        if (elevatorState == ElevatorState.HATCHLOW) {
            elevatorState = ElevatorState.PORTLOW
        } else if (elevatorState == ElevatorState.HATCHMID) {
            elevatorState = ElevatorState.PORTMID
        } else if (elevatorState == ElevatorState.HATCHHIGH) {
            elevatorState = ElevatorState.PORTHIGH
        } else if (elevatorState == ElevatorState.PORTLOW) {
            elevatorState = ElevatorState.HATCHLOW
        } else if (elevatorState == ElevatorState.PORTMID) {
            elevatorState = ElevatorState.HATCHMID
        } else if (elevatorState == ElevatorState.PORTHIGH) {
            elevatorState = ElevatorState.HATCHHIGH
        }
    }

    fun updatePosition (wantsUp: Boolean) {
        if (wantsUp) {
            if (isHatchPanel) {
                if (elevatorState == ElevatorState.HATCHLOW) {
                    elevatorState = ElevatorState.HATCHMID
                } else if (elevatorState == ElevatorState.HATCHMID) {
                    elevatorState = ElevatorState.HATCHHIGH
                }
            } else {
                if (elevatorState == ElevatorState.PORTLOW) {
                    elevatorState = ElevatorState.PORTMID
                } else if (elevatorState == ElevatorState.PORTMID) {
                    elevatorState = ElevatorState.PORTHIGH
                }
            }
        } else {
            if (isHatchPanel) {
                if (elevatorState == ElevatorState.HATCHHIGH) {
                    elevatorState = ElevatorState.HATCHMID
                } else if (elevatorState == ElevatorState.HATCHMID) {
                    elevatorState = ElevatorState.HATCHLOW
                }
            } else {
                if (elevatorState == ElevatorState.PORTHIGH) {
                    elevatorState = ElevatorState.PORTMID
                } else if (elevatorState == ElevatorState.PORTMID) {
                    elevatorState = ElevatorState.PORTLOW
                }
            }
        }
    }

    fun setElevatorPosition(position: Double) {
        var target = position
        if (target == Double.NaN) {
            target = observedElevatorPosition
        } else {
            //observedElevatorPosition = target
        }
        talon.set(ControlMode.MotionMagic, ElevatorConversion.inchesToPulses(target).toDouble())
        println("POSITION: " + observedElevatorPosition)
        println("TARGET: " + target)
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            elevatorState = ElevatorState.OPEN_LOOP
            isHatchPanel = false
        }

        override fun onLoop() {
            synchronized(this@Elevator) {
                observedElevatorVelocity = ElevatorConversion.nativeSpeedToInchesPerSecond(talon.sensorCollection.quadratureVelocity.toDouble())
                observedElevatorPosition = ElevatorConversion.pulsesToInches(talon.sensorCollection.quadraturePosition.toDouble())
                if(observedElevatorVelocity > maxElevatorV){
                    maxElevatorV = observedElevatorVelocity
                }
                println("Max ElevatorV: " + maxElevatorV)
                elevatorPower = -talon.motorOutputPercent

                println("elevatorPos: " +  observedElevatorPosition)

                when (elevatorState){
                    ElevatorState.OPEN_LOOP -> {
                        //setOpenLoop(wantedElevatorPower)
                        return
                    }
                    ElevatorState.VELOCITY_CONTROL -> {
                        return
                    }
                    else -> {
                        setElevatorPosition(elevatorState.targetPos)
                    }
                }
               // climberLatch.set(if (elevatorState == ElevatorState.CLIMBING) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
                when {
                    observedElevatorVelocity in -1 .. 1 -> movementState = MovementState.STILL
                    observedElevatorVelocity > 1 -> movementState = MovementState.UP
                    observedElevatorVelocity < 1 -> movementState = MovementState.DOWN
                }
                //println()
            }
        }

        override fun onStop() = stop()
    }

    override fun stop() {
        movementState = MovementState.STILL
        setElevatorVelocity(0.0)

    }

    companion object {
        val instance = Elevator()
    }

    override fun zeroSensors() {
        talon.sensorCollection.setQuadraturePosition(0, 0)
        slave.setSelectedSensorPosition(0,0,0)
    }
}