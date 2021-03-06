package com.ciarandg.soundbounds.client

import com.ciarandg.soundbounds.SoundBounds
import com.ciarandg.soundbounds.client.options.SBClientOptions
import java.util.Observable
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs
import kotlin.math.max

class Fader(
    private val postFadeCallback: () -> Unit
) : Observable() {
    private val state = State(MAX_GAIN, false)
    private val timer = Timer()
    private var incrementFadeTask = IncrementFadeTask(this)
    private var gainPerTick = fetchGainPerTick()

    fun requestFade() {
        if (!state.isFading) startFade()
    }

    fun reset() {
        synchronized(state) {
            if (state.isFading) stopFade()
            state.gain = MAX_GAIN
            notifyGainChange()
        }
    }

    private fun stopFade() {
        synchronized(state) {
            incrementFadeTask.cancel()
            incrementFadeTask = IncrementFadeTask(this)
            state.isFading = false
        }
    }

    fun getGain() = state.gain

    private fun startFade() {
        synchronized(state) {
            gainPerTick = fetchGainPerTick()
            state.isFading = true
            if (state.gain == MIN_GAIN) SoundBounds.LOGGER.warn("Attempting to fade from silence")
            timer.scheduleAtFixedRate(incrementFadeTask, 0, TICK_LENGTH_MS)
        }
    }

    private fun incrementFade() {
        synchronized(state) {
            state.gain = max(MIN_GAIN, state.gain - gainPerTick)
            notifyGainChange()
            if (state.gain == MIN_GAIN) {
                state.isFading = false
                stopFade()
                postFadeCallback()
            }
        }
    }

    private fun notifyGainChange() {
        setChanged()
        notifyObservers(state.gain)
    }

    private fun fetchGainPerTick() =
        TICK_LENGTH_MS.toFloat() / SBClientOptions.data.fadeDuration.toFloat() * abs(MAX_GAIN - MIN_GAIN)

    companion object {
        private const val MAX_GAIN: Float = 1.0f
        private const val MIN_GAIN: Float = 0.0f
        private const val TICK_LENGTH_MS: Long = 20
    }

    private data class State(var gain: Float, var isFading: Boolean)

    private class IncrementFadeTask(val owner: Fader) : TimerTask() {
        override fun run() = owner.incrementFade()
    }
}
