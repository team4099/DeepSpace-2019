package org.usfirst.frc.team4099

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
//import main.java.org.usfirst.frc.team4099.lib.util.AutoModeCreator
import org.usfirst.frc.team4099.auto.modes.*
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.Constants.Autonomous.AUTO_OPTIONS_DASHBOARD_KEY
import org.usfirst.frc.team4099.robot.Constants.Autonomous.AUTO_STARTS_DASHBOARD_KEY
import org.usfirst.frc.team4099.robot.Constants.Autonomous.SELECTED_AUTO_MODE_DASHBOARD_KEY
import org.usfirst.frc.team4099.robot.Constants.Autonomous.SELECTED_AUTO_START_DELAY_KEY
import org.usfirst.frc.team4099.robot.Constants.Autonomous.SELECTED_AUTO_START_POS_KEY
import org.usfirst.frc.team4099.robot.subsystems.Drive
import org.usfirst.frc.team4099.robot.subsystems.Elevator
import org.usfirst.frc.team4099.robot.subsystems.Climber
import org.usfirst.frc.team4099.robot.subsystems.Intake


/**
 * Controls the interactive elements of SmartDashboard.
 *
 * Keeps the network tables keys in one spot and enforces autonomous mode
 * invariants.
 */
object DashboardConfigurator {

    private val climber = Climber.instance
    private val elevator = Elevator.instance
    private val intake = Intake.instance
    //private val wrist = Wrist.instance

    var inst: NetworkTableInstance = NetworkTableInstance.getDefault()
    var table = inst.getTable("datatable")
    var climberStateEntry = table.getEntry("Climber State")
    var elevatorPositionEntry = table.getEntry("Elevator Position")
    var intakeStateEntry = table.getEntry("Intake State")
    var intakeModeEntry = table.getEntry("Intake Mode")
    var hatchPanelStateEntry = table.getEntry("Hatch Panel State")

    enum class StartingPosition(val dashboardName: String)  { //TODO: Investigate starting locations
        LEFT("LEFT"),
        CENTER("CENTER"),
        RIGHT("RIGHT")
    }



    fun initDashboard() {

    }

    fun updateValues() {
        climberStateEntry.setString(climber.climberState.toString)
        elevatorPositionEntry.setDouble(elevator.observedElevatorPosition)
        intakeStateEntry.setString(if(intake.intakeState == Intake.IntakeState.IN) "In" else "Out")
        intakeModeEntry.setString(if(elevator.isHatchPanel) "Hatch" else "Cargo")
        //hatchPanelStateEntry.setString()
    }


}