package com.pluto.plugin.utilities

import android.util.Log

class DebugLog private constructor() {

    companion object {
        var enabled: Boolean = true

        fun v(tag: String, message: String?, tr: Throwable? = null) {
            if (enabled) {
                Log.v(tag, message.toString(), tr)
            }
        }

        fun d(tag: String, message: String?, tr: Throwable? = null) {
            if (enabled) {
                Log.d(tag, message.toString(), tr)
            }
        }

        fun i(tag: String, message: String?, tr: Throwable? = null) {
            if (enabled) {
                Log.i(tag, message.toString(), tr)
            }
        }

        fun w(tag: String, message: String?, tr: Throwable? = null) {
            if (enabled) {
                Log.w(tag, message.toString(), tr)
            }
        }

        fun e(tag: String, message: String?, tr: Throwable? = null) {
            if (enabled) {
                Log.e(tag, message, tr)
            }
        }
    }
}
