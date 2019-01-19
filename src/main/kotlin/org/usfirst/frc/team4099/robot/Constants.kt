package org.usfirst.frc.team4099.robot

class Constants {
    object Climber {
        val CLIMBER_F1_FORWARD_ID = 0
        val CLIMBER_F1_REVERSE_ID = 1
        val CLIMBER_F2_FORWARD_ID = 2
        val CLIMBER_F2_REVERSE_ID = 3
        val CLIMBER_B1_FORWARD_ID = 4
        val CLIMBER_B1_REVERSE_ID = 5
        val CLIMBER_B2_FORWARD_ID = 6
        val CLIMBER_B2_REVERSE_ID = 7
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
      
    object Intake {
        val INTAKE_TALON_ID = 0
        val  SHIFTER_FORWARD_ID = 1
        val SHIFTER_REVERSE_ID = 2
    }

    object Joysticks {
        val DRIVER_PORT = 0
        val SHOTGUN_PORT = 1
    }

}
