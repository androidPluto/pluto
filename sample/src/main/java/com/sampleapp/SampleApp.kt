package com.sampleapp

import android.app.Application
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.network.PlutoNetworkPlugin

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

//        val samplePlugin = object : Plugin() {
//            override fun getConfig(): PluginConfiguration {
//                return PluginConfiguration(
//                    name = "sample module _Aosdfm",
//                )
//            }
//
//            override fun getDeveloperDeveloperDetails(): DeveloperDetails? {
//                return null
//            }
//
//            override fun onPluginInstalled() {
//                Log.d("Prateek", getConfig().identifier)
//            }
//        }
        Pluto.Installer(this)
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
