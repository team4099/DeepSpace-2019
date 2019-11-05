package org.usfirst.frc.team4099.robot

class Constants {


    object Drive {
        val LEFT_MASTER_ID = 10
        val LEFT_SLAVE_1_ID = 9
        // val LEFT_SLAVE_2_ID = 5
        val RIGHT_MASTER_ID = 5
        val RIGHT_SLAVE_1_ID = 6
        // val RIGHT_SLAVE_2_ID = 10

        val HIGH_GEAR_MAX_SETPOINT = 17.0  //17 fps

        val MAX_LEFT_OPENLOOP_VEL = 1.0
        val MAX_RIGHT_OPENLOOP_VEL = 1.0

        val SHIFTER_FORWARD_ID = 7
        val SHIFTER_REVERSE_ID = 0

        val LEFT_KV_FORWARD_HIGH = 0.4993
        val RIGHT_KV_FORWARD_HIGH = 0.5412

        val LEFT_KA_FORWARD_HIGH = 0.0468
        val RIGHT_KA_FORWARD_HIGH = 0.0601

        val LEFT_V_INTERCEPT_FORWARD_HIGH = 0.1879
        val RIGHT_V_INTERCEPT_FORWARD_HIGH = 0.1364

        val LEFT_KV_REVERSE_HIGH = 0.4987
        val RIGHT_KV_REVERSE_HIGH = 0.5194

        val LEFT_KA_REVERSE_HIGH = 0.0372
        val RIGHT_KA_REVERSE_HIGH = 0.0644

        val LEFT_V_INTERCEPT_REVERSE_HIGH = -0.1856
        val RIGHT_V_INTERCEPT_REVERSE_HIGH = -0.2003

        val FEET_PER_SEC_TO_RPM = 6.8 * 60.0 //10.4


    }

    object Wheels {
        val DRIVE_WHEEL_DIAMETER_INCHES = 6
    }



    object Gains {
        val LEFT_LOW_KP = 0.0000//.1 * 1500 / 70
        val LEFT_LOW_KI = 0.0000
        val LEFT_LOW_KD = 0.0000
        val LEFT_LOW_KF = 0.0000//1023.0 / 2220.0

        //subject to change
        val LEFT_HIGH_KP = 0.0000//.1 * 1023 / 70
        val LEFT_HIGH_KI = 0.0000
        val LEFT_HIGH_KD = 0.0000
        val LEFT_HIGH_KF = 0.0000//1023.0 / 4420.0

        val RIGHT_LOW_KP = 0.0000//.1 * 1500 / 70
        val RIGHT_LOW_KI = 0.0000
        val RIGHT_LOW_KD = 0.0000
        val RIGHT_LOW_KF = 0.0000//1023.0 / 2220.0

        //subject to change
        val RIGHT_HIGH_KP = 0.0000//.1 * 1023 / 70
        val RIGHT_HIGH_KI = 0.0000
        val RIGHT_HIGH_KD = 0.0000
        val RIGHT_HIGH_KF = 0.0000//1023.0 / 4420.0


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
        val NUMBER_OF_TRIES = 5 ;
    }
    object Joysticks {
        val DRIVER_PORT = 0
        val SHOTGUN_PORT = 1
    }

}
