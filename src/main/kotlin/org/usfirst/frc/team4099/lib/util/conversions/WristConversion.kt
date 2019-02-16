package org.usfirst.frc.team4099.lib.util.conversions

import kotlin.math.PI


object WristConversion {
    val pulsesToRadians = 2 * PI / 1024//18/1024/84/2/PI

    fun radiansToPulses(radians: Double): Double {
        return (radians / pulsesToRadians)
    }

    fun pulsesToRadians(pulses: Double): Double {
        return pulses * pulsesToRadians
}
}