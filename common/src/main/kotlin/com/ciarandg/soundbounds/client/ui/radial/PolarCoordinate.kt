package com.ciarandg.soundbounds.client.ui.radial

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class PolarCoordinate(val radius: Double, val angle: Double) {
    fun toCartesian(): Pair<Double, Double> = Pair(radius * cos(angle), radius * sin(angle))

    companion object {
        fun fromCartesian(x: Double, y: Double) = PolarCoordinate(radiusFromCartesian(x, y), angleFromCartesian(x, y))

        private fun radiusFromCartesian(x: Double, y: Double) = hypot(x, y).let {
            when {
                it.isNaN() -> maxOf(x, y)
                else -> it
            }
        }

        private fun angleFromCartesian(x: Double, y: Double) = atan2(x, y).let { if (it < 0.0) it + 2.0 * PI else it }
    }
}