package com.pluto

import android.app.Application
import android.os.Bundle

@SuppressWarnings("EmptyFunctionBlock", "UnusedPrivateMember")
object Pluto {

    @JvmOverloads
    fun open(identifier: String? = null, bundle: Bundle? = null) {
    }

    fun showNotch(state: Boolean) {
    }

    fun clearAllLogs() {}

    fun clearLogs(plugin: Any) {}

    class Installer(private val application: Application) {

        private val plugins = linkedSetOf<Any>()

        fun addPlugin(plugin: Any): Installer {
            return this
        }

        fun install() {
        }
    }
}
