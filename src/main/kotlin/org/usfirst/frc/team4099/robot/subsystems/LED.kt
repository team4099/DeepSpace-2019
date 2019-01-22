package org.usfirst.frc.team4099.robot.subsystems

import org.usfirst.frc.team4099.lib.util.Photon
import org.usfirst.frc.team4099.robot.loops.Loop

class LED private constructor() : Subsystem  {
    private val photon: Photon
    private val numStrips = 2

    enum class SystemState {
        OFF, AUTO, HATCH, CARGO, ALIGNING, BACK_DOWN, FRONT_DOWN, UNJAM, INTAKE_IN
    }

    var systemState = SystemState.OFF

    init {
        photon = Photon()
        for (i in 0..numStrips) {
            photon.SetNumberOfLEDs(i, 60)
        }

    }
    val loop: Loop = object : Loop {
        override fun onStart() {
            systemState = SystemState.OFF
            for (i in 0..numStrips) {
                photon.setAnimation(i, Photon.Animation.OFF)
            }
        }

        override fun onLoop() {
            synchronized(this@LED) {
                when (systemState) {
                    SystemState.OFF -> handleOff()
                    SystemState.AUTO -> handleAuto()
                    SystemState.HATCH -> handleHatch()
                    SystemState.CARGO -> handleCargo()
                    SystemState.ALIGNING -> handleAlign()
                    SystemState.FRONT_DOWN -> handleFrontDown()
                    SystemState.BACK_DOWN -> handleBackDown()
                    SystemState.UNJAM -> handleUnjam()
                    SystemState.INTAKE_IN -> handleIntake()
                }
            }
        }

        override fun onStop() {
            for (i in 0..numStrips) {
                photon.setAnimation(i, Photon.Animation.OFF)
            }
        }
    }

    fun setState(state: SystemState) {
        this.systemState = state
    }

    fun handleOff() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.OFF)
        }
    }

    fun handleAuto() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.PULSE_DUAL, Photon.Color.YELLOW, Photon.Color.ORANGE)
        }
    }

    fun handleHatch() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.SOLID, Photon.Color.WHITE)
        }
    }

    fun handleCargo() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.SOLID, Photon.Color.ORANGE)
        }
    }

    fun handleAlign() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.SOLID, Photon.Color.PURPLE)
        }
    }

    fun handleFrontDown() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.BLINK, Photon.Color.AQUA)
        }
    }

    fun handleBackDown() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.BLINK, Photon.Color.GREEN)
        }
    }

    fun handleUnjam() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.CYLON, Photon.Color.ORANGE)
        }
    }

    fun handleIntake() {
        for (i in 0..numStrips) {
            photon.setAnimation(i, Photon.Animation.BLINK, Photon.Color.GREEN)
        }
    }

    companion object {
        val instance = LED()
    }
    override fun outputToSmartDashboard() {}

    override fun stop() {}

    override fun zeroSensors() {}
}