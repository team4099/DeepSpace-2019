package org.usfirst.frc.team4099.lib.util.conversions

object ElevatorConversion : EncoderConversion {
    val pulsesToInches = 1.5 * Math.PI * 3.0 / 1024.0//1.0/907.0 / .526  // diameter * circumference * stages / pulses per revolution

    override fun inchesToPulses(inches: Double): Double {
        return (inches / pulsesToInches)
    }

    override fun pulsesToInches(pulses: Double): Double {
        return pulses * pulsesToInches
    }

    override fun inchesPerSecondtoNativeSpeed(ips: Double): Double {
        return inchesToPulses(ips) / 10
    }

    override fun nativeSpeedToInchesPerSecond(nativeSpeed: Double): Double {
        return pulsesToInches(nativeSpeed) * 10
    }
}