package org.usfirst.frc.team4099.robot.subsystems

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4099.robot.Constants
import org.usfirst.frc.team4099.robot.loops.BrownoutDefender
import org.usfirst.frc.team4099.robot.loops.Loop

class Grabber private constructor() : Subsystem{
    private var pushStartTime = 0.0
    var push = false
        set (wantsPush) {
            if (wantsPush) {
                pushStartTime = (System.currentTimeMillis()).toDouble()
            } else {
            }
            field = wantsPush
        }

    override fun outputToSmartDashboard() {
        SmartDashboard.putBoolean("intake/isPushed", push)
        SmartDashboard.putNumber("intake/current", BrownoutDefender.instance.getCurrent(7))
    }

    @Synchronized override fun stop() {
    }

    val loop: Loop = object : Loop {
        override fun onStart() {
            push = false
        }

        override fun onLoop() {
            synchronized(this@Grabber) {
                if(push == true && System.currentTimeMillis() + 100 > pushStartTime){
                    push = false
                }
            }
        }

        override fun onStop() = stop()
    }

    companion object {
        val instance = Grabber()
    }

    override fun zeroSensors() { }
}