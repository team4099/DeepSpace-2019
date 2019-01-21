package org.usfirst.frc.team4099.robot

class Constants {

    object Intake {
        val INTAKE_TALON_ID = 0
        val  SHIFTER_FORWARD_ID = 1
        val SHIFTER_REVERSE_ID = 2
    }

    object Joysticks {
        val DRIVER_PORT = 0
        val SHOTGUN_PORT = 1
    }
  
    object Gains {
        //subject to change
        val ELEVATOR_UP_KP = 0.6
        val ELEVATOR_UP_KI = 0.0008
        val ELEVATOR_UP_KD = 55.000
        val ELEVATOR_UP_KF = 0.5700

        val ELEVATOR_DOWN_KP = 1.00
        val ELEVATOR_DOWN_KI = 0.002
        val ELEVATOR_DOWN_KD = 60.0
        val ELEVATOR_DOWN_KF = 0.78
    }

    object Elevator {
        val ELEVATOR_TALON_ID = 0
    }

    object Drive {
        val LEFT_MASTER_ID = 4
        val LEFT_SLAVE_1_ID = 5
        val LEFT_SLAVE_2_ID = 6
        val RIGHT_MASTER_ID = 8
        val RIGHT_SLAVE_1_ID = 9
        val RIGHT_SLAVE_2_ID = 10

        val HIGH_GEAR_MAX_SETPOINT = 17.0  //17 fps

        val SHIFTER_FORWARD_ID = 0
        val SHIFTER_REVERSE_ID = 1
    }

    object Vision {
        val Kp = -0.1
        val minCommand = 0.05
        val CAMERA_TO_TARGET_HEIGHT = 20;
        val CAMERA_ANGLE = 0;
    }

}
