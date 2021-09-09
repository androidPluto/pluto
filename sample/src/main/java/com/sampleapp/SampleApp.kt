package com.sampleapp

import android.app.Application
import android.util.Log
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.PlutoLog
import com.mocklets.pluto.modules.exceptions.ANRException
import com.mocklets.pluto.modules.exceptions.ANRListener

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.initialize(this)
        Pluto.setANRListener(object : ANRListener {
            override fun onAppNotResponding(exception: ANRException) {
                exception.printStackTrace()
                PlutoLog.e("anr-exception", exception.threadStateMap)
            }
        })
        Pluto.setExceptionHandler { thread, tr ->
            Log.d("prateek", "uncaught exception : " + thread.name, tr)
        }
    }
}
