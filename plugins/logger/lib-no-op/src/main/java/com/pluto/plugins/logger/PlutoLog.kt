package com.pluto.plugins.logger

import androidx.annotation.Keep

@Keep
@SuppressWarnings("UnusedPrivateMember", "EmptyFunctionBlock")
class PlutoLog private constructor() {

    companion object {

        @JvmStatic
        fun v(tag: String, message: String, tr: Throwable? = null) {}

        @JvmStatic
        fun d(tag: String, message: String, tr: Throwable? = null) {}

        @JvmStatic
        fun i(tag: String, message: String, tr: Throwable? = null) {}

        @JvmStatic
        fun w(tag: String, message: String, tr: Throwable? = null) {}

        @JvmStatic
        fun e(tag: String, message: String, tr: Throwable? = null) {}

        @JvmStatic
        fun wtf(tag: String, message: String, tr: Throwable? = null) {}

        @JvmStatic
        fun event(tag: String, event: String, attributes: HashMap<String, Any?>?) {}
    }
}
