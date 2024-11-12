package com.pluto.plugin.libinterface

import android.app.Application
import androidx.fragment.app.FragmentActivity
import java.lang.IllegalStateException

class PlutoInterface private constructor(
    val application: Application,
    val pluginActivityClass: Class<out FragmentActivity>,
    val selectorActivityClass: Class<out FragmentActivity>
) {

    companion object {
        private var instance: PlutoInterface? = null

        private val get: PlutoInterface
            get() {
                if (instance != null) {
                    return instance!!
                }
                throw IllegalStateException("PluginUiBridge not initialised yet")
            }

        val notification: NotificationInterface
            get() = NotificationInterface(get.application, get.pluginActivityClass)

        val libInfo: LibraryInfoInterface
            get() = LibraryInfoInterface(get.pluginActivityClass, get.selectorActivityClass)

        val files: FilesInterface by lazy { FilesInterface(get.application) } // singleton

        fun create(
            application: Application,
            pluginActivityClass: Class<out FragmentActivity>,
            selectorActivityClass: Class<out FragmentActivity>
        ): PlutoInterface? {
            if (instance == null) {
                synchronized(PlutoInterface::class.java) {
                    if (instance == null) {
                        instance = PlutoInterface(application, pluginActivityClass, selectorActivityClass)
                    }
                }
            }
            return instance
        }
    }
}
