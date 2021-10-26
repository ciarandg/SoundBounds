package com.ciarandg.soundbounds.client.ui.baton

import kotlin.math.max
import kotlin.math.min

interface ICursorMode {
    val range: Double
}

class UnboundedCursorMode : ICursorMode {
    override val range = 100.0
}

class RadiusCursorMode : ICursorMode {
    override var range: Double = DEFAULT_RANGE
        set(value) {
            field = min(max(value, MIN_RANGE), MAX_RANGE)
        }

    companion object {
        const val DEFAULT_RANGE = 10.0
        const val MAX_RANGE = 20.0
        const val MIN_RANGE = 0.0
    }
}
