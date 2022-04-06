package com.pluto.plugins.utilities

import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import android.view.View
import com.pluto.plugins.utilities.ClickUtils.Companion.DEBOUNCE_DELAY
import com.pluto.plugins.utilities.ClickUtils.Companion.enabled

fun View.setDebounceClickListener(
    delay: Long = DEBOUNCE_DELAY,
    haptic: Boolean = false,
    action: ((View) -> Unit)?
) {
    action?.let {
        setOnClickListener { view ->
            view.onDebounce(delay) {
                view.hapticFeedback(haptic)
                view.soundFeedback()
                it.invoke(view)
            }
        }
        return
    }
    setOnClickListener(null)
}

private inline fun View.onDebounce(delay: Long, next: () -> Unit?) {
    if (enabled) {
        enabled = false
        postDelayed({ enabled = true }, delay)
        next()
    }
}

fun View.hapticFeedback(isGlobal: Boolean) {
    isHapticFeedbackEnabled = true
    performHapticFeedback(
        HapticFeedbackConstants.VIRTUAL_KEY,
        if (isGlobal) HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING else HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
    )
}

fun View.soundFeedback() {
    isSoundEffectsEnabled = true
    playSoundEffect(SoundEffectConstants.CLICK)
}

internal class ClickUtils private constructor() {
    companion object {
        var enabled: Boolean = true
        const val DEBOUNCE_DELAY = 250L
    }
}
