package com.mocklets.pluto.core

import android.util.Log
import com.mocklets.pluto.BuildConfig

internal class DebugLog private constructor() {

    companion object {
        fun v(tag: String, message: String?, tr: Throwable? = null) {
            if (BuildConfig.DEBUG) {
                Log.v(tag, message.toString(), tr)
            }
        }

        fun d(tag: String, message: String?, tr: Throwable? = null) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message.toString(), tr)
            }
        }

        fun i(tag: String, message: String?, tr: Throwable? = null) {
            if (BuildConfig.DEBUG) {
                Log.i(tag, message.toString(), tr)
            }
        }

        fun w(tag: String, message: String?, tr: Throwable? = null) {
            if (BuildConfig.DEBUG) {
                Log.w(tag, message.toString(), tr)
            }
        }

        fun e(tag: String, message: String?, tr: Throwable? = null) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message, tr)
            }
        }
    }
}
