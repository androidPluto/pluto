package com.pluto

import androidx.annotation.Keep

@Keep
object PlutoLog {

    fun v(tag: String, message: String?, tr: Throwable? = null) {}

    fun d(tag: String, message: String?, tr: Throwable? = null) {}

    fun i(tag: String, message: String?, tr: Throwable? = null) {}

    fun w(tag: String, message: String?, tr: Throwable? = null) {}

    fun e(tag: String, message: String?, tr: Throwable? = null) {}

    fun event(tag: String, event: String, attributes: HashMap<String, Any?>?) {}
}
