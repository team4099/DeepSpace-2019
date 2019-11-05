package org.usfirst.frc.team4099.robot.subsystems

import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import org.usfirst.frc.team4099.robot.Constants

class BallIntake: Subsystem {

    private val talon = TalonSRX(Constants.Intake.INTAKE_TALON_ID)

    enum class IntakeStage {
        IN, STOP
    }
    var intakeStage = IntakeStage.STOP
    init {
        talon.configPeakCurrentLimit(20) // Max amount of current you can send to the motor.
        talon.setNeutralMode(NeutralMode.Coast)
        talon.inverted = false
    }

    fun outputToSmartDashboard()
    fun stop()
    fun zeroSensors()


}