package org.usfirst.frc.team4099.robot

class Constants {


    object Drive {
        val LEFT_MASTER_ID = 7
        val LEFT_SLAVE_1_ID = 6
        val LEFT_SLAVE_2_ID = 5
        val RIGHT_MASTER_ID = 8
        val RIGHT_SLAVE_1_ID = 9
        val RIGHT_SLAVE_2_ID = 10

        val HIGH_GEAR_MAX_SETPOINT = 17.0  //17 fps

        val SHIFTER_FORWARD_ID = 2
        val SHIFTER_REVERSE_ID = 5
    }

    object Wheels {
        val DRIVE_WHEEL_DIAMETER_INCHES = 6
    }

    object Climber {
        val SHIFTER_FORWARD_ID = 0
        val SHIFTER_REVERSE_ID = 0
        val CLIMBER_SPARK_ID = 0
        val DRIVE_SPARK_ID = 0
        val CLIMBER_KP = 0.0
        val CLIMBER_KI = 0.0
        val CLIMBER_KD = 0.0
        val CLIMBER_KIz = 0.0
        val CLIMBER_KF = 0.0
        val MAX_OUTPUT = 1.0
        val DOWN_POSITION = 0.0
        val UP_POSITION = 0.0
    }


    object Gains {
        val LEFT_LOW_KP = .1 * 1500 / 70
        val LEFT_LOW_KI = 0.0000
        val LEFT_LOW_KD = 0.0000
        val LEFT_LOW_KF = 1023.0 / 2220.0

        //subject to change
        val LEFT_HIGH_KP = .1 * 1023 / 70
        val LEFT_HIGH_KI = 0.0000
        val LEFT_HIGH_KD = 0.0000
        val LEFT_HIGH_KF = 1023.0 / 4420.0

        val RIGHT_LOW_KP = .1 * 1500 / 70
        val RIGHT_LOW_KI = 0.0000
        val RIGHT_LOW_KD = 0.0000
        val RIGHT_LOW_KF = 1023.0 / 2220.0

        //subject to change
        val RIGHT_HIGH_KP = .1 * 1023 / 70
        val RIGHT_HIGH_KI = 0.0000
        val RIGHT_HIGH_KD = 0.0000
        val RIGHT_HIGH_KF = 1023.0 / 4420.0

        val ELEVATOR_UP_KP = 3.0
        val ELEVATOR_UP_KI = 0.0005
        val ELEVATOR_UP_KD = 150.0
        val ELEVATOR_UP_KF = 2.0

        val ELEVATOR_DOWN_KP = 2.0
        val ELEVATOR_DOWN_KI = 0.0
        val ELEVATOR_DOWN_KD = 150.0
        val ELEVATOR_DOWN_KF = 1.0

        val ELEVATOR_UP_KP_V = 0.4
        val ELEVATOR_UP_KI_V = 0.0
        val ELEVATOR_UP_KD_V = 0.0
        val ELEVATOR_UP_KF_V = 0.3

        val ELEVATOR_DOWN_KP_V = 0.3
        val ELEVATOR_DOWN_KI_V = 0.00
        val ELEVATOR_DOWN_KD_V = 0.0
        val ELEVATOR_DOWN_KF_V = 0.1

    }


    object Elevator {
        val ELEVATOR_TALON_ID = 3   //CHANGE
        val SLAVE_VICTOR_ID = 13 //CHANGE
        val MAX_SPEED = 180 //CHANGE
        val MIN_TRIGGER = 0.1
        val BOTTOM_SOFT_LIMIT = 0.0 //set later - CHANGE
    }




    object Vision {
        val Kp = -0.02
        val minCommand = 0
        val HATCH_PANEL_HEIGHT = 20 // inches
        val CARGO_HEIGHT = 20 // inches
        val CAMERA_ANGLE = 0 // degrees, NOT SET
        val TARGET_HEIGHT_ADJUST = 10 //inches, NOT SET
    }
    object Velocity {
        val HIGH_GEAR_VELOCITY_CONTROL_SLOT = 0
        val LOW_GEAR_VELOCITY_CONTROL_SLOT = 0
        val DRIVE_HIGH_GEAR_NOMINAL_OUTPUT = 0.0 //percentage
        val DRIVE_LOW_GEAR_NOMINAL_OUTPUT = 0.0 //percentage
        val DRIVE_HIGH_GEAR_MAX_FORWARD_OUTPUT = 1.0 //percentage
        val DRIVE_HIGH_GEAR_MAX_REVERSE_OUTPUT = -1.0
        val DRIVE_LOW_GEAR_MAX_FORWARD_OUTPUT = 1.0
        val DRIVE_LOW_GEAR_MAX_REVERSE_OUTPUT = -1.0

        val SHIFTER_CHANNEL = 0
        val SHIFTER_MODULE = 0
        val MAX_HIGH_VELOCITY = 72 //change
        val MAX_HIGH_ACCEL =  78.2//change
        val MAX_HIGH_JERK = 2000 //change

        val MAX_LOW_VELOCITY = 72 //change
        val MAX_LOW_ACCEL = 78.2 //change
        val MAX_LOW_JERK = 2000 //change

        val HIGH_KV = 1/MAX_HIGH_VELOCITY
        val HIGH_KA = 0

        val LOW_KV = 1/ MAX_LOW_VELOCITY
        val LOW_KA = 0
    }
    object Loopers {
        val LOOPER_DT = 0.02 // 50 Hz
    }

    object Dashboard {
        val ALLIANCE_COLOR_KEY = "dashboard/allianceColor"
        val ALLIANCE_OWNERSHIP_KEY = "dashboard/allianceOwnership"
    }

    object Autonomous {
        val AUTO_OPTIONS_DASHBOARD_KEY = "autonomous/autoOptions"
        val SELECTED_AUTO_MODE_DASHBOARD_KEY = "autonomous/selectedMode"

        val AUTO_STARTS_DASHBOARD_KEY = "autonomous/autoStarts"
        val SELECTED_AUTO_START_POS_KEY = "autonomous/selectedStart"

        val SELECTED_AUTO_START_DELAY_KEY = "autonomous/selectedDelay"

        val CONNECTION_TIMEOUT_MILLIS = 1000
        val NUMBER_OF_TRIES = 5
    }
    object Joysticks {
        val DRIVER_PORT = 0
        val SHOTGUN_PORT = 1
    }

    object Intake {
        val INTAKE_TALON_ID = 12
        val EXTENDER_FORWARD_ID = 6
        val EXTENDER_REVERSE_ID = 1
        val DEPLOYER_FORWARD_ID = 7
        val DEPLOYER_REVERSE_ID = 0
    }


    object Wrist {
        val WRIST_TALON_ID = 15 //MAY NOT MATCH FINAL BOT FIX LATER
        val WRIST_SLAVE_VICTOR_ID = 14
        val MAX_SPEED = 2.485

        val WRIST_UP_KP = 0.7
        val WRIST_UP_KI = 0.0
        val WRIST_UP_KD = 85.0
        val WRIST_UP_KF = 1.75

        val WRIST_DOWN_KP = 0.7
        val WRIST_DOWN_KI = 0.0
        val WRIST_DOWN_KD = 85.0
        val WRIST_DOWN_KF = 1.75

        val WRiST_VELOCITY_KP = 0.35
        val WRiST_VELOCITY_KI = 0.0
        val WRiST_VELOCITY_KD = 0.0
        val WRiST_VELOCITY_KF = 2.4
    }
}
