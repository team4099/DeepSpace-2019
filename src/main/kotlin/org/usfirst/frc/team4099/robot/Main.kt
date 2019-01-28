package org.usfirst.frc.team4099.robot

import edu.wpi.first.wpilibj.RobotBase
import org.usfirst.frc.team4099.robot.Robot
import java.util.function.Supplier

public class Main {
    // DO NOT EDIT (does not run without this)
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            RobotBase.startRobot(Supplier<Robot> { Robot() })
        }
    }
}