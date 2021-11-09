package com.sampleapp

import android.app.Application
import android.util.Log
import com.mocklets.pluto.module.Pluto
import com.mocklets.pluto.network.PlutoNetworkPlugin
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginConfiguration

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val samplePlugin = object : Plugin() {
            override fun getConfig(): PluginConfiguration {
                return PluginConfiguration(
                    name = "sample module _Aosdfm"
                )
            }

            override fun onPluginInstalled() {
                Log.d("Prateek", getConfig().identifier)
            }
        }
        Pluto.Installer(this)
            .addPlugin(samplePlugin)
            .addPlugin(PlutoNetworkPlugin())
            .install()
//        Pluto.initialize(this)
//        Pluto.setANRListener(object : ANRListener {
//            override fun onAppNotResponding(exception: ANRException) {
//                exception.printStackTrace()
//                PlutoLog.e("anr-exception", exception.threadStateMap)
//            }
//        })
//        Pluto.setExceptionHandler { thread, tr ->
//            Log.d("exception", "uncaught exception handled on thread: " + thread.name, tr)
//        }
    }
}
