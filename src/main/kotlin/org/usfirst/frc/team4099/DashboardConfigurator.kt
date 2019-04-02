package org.usfirst.frc.team4099

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


/**
 * Controls the interactive elements of SmartDashboard.
 *
 * Keeps the network tables keys in one spot and enforces autonomous mode
 * invariants.
 */
object DashboardConfigurator {

    un getAutonomousMode(): String {
        val selectedIntakeMode = SmartDashboard.getString('/SmartDashboard/autonomous/selectedAutoMode', "empty")
        return selectedIntakeMode
    }

    fun getStartingPosition(): StartingPosition {
        var selectedStartingPosition = SmartDashboard.getString('/SmartDashboard/autonomous/selectedPosition', "empty")
        if (selectedStartingPosition == "left") {
            selectedStartingPosition = StartingPosition.LEFT
        }
        else if (selectedStartingPosition == "center") {
            selectedStartingPosition = StartingPosition.CENTER
        }
        else if (selectedStartingPosition == "right") {
            selectedStartingPosition = StartingPosition.RIGHT
        }
        return selectedStartingPosition
    }

    fun getStartingHeight(): StartingHeight {
        var selectedStartingHeight = SmartDashboard.getString('/SmartDashboard/autonomous/selectedHeight', "empty")
        if (selectedStartingHeight == "one") {
            selectedStartingHeight = StartingHeight.H1
        }
        else if (selectedStartingPosition == "two") {
            selectedStartingHeight = StartingHeight.H2
        }
        return selectedStartingHeight
    }

    enum class StartingPosition(val dashboardName: String)  { //TODO: Investigate starting locations
        LEFT("LEFT"),
        CENTER("CENTER"),
        RIGHT("RIGHT")
    }

    enum class StartingHeight(val dashboardName: String){
        H1("H1"),
        H2("H2")
    }
    enum class DesiredRocketSide(val dashboardName: String){
        LEFT("LEFT"),
        RIGHT("RIGHT")
    }

    fun getIntakeMode() : String{
        return SmartDashboard.getString("intakeStartingState", "")
    }

    fun initDashboard() {

    }
    
}