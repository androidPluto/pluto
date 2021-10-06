package com.sampleapp

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.PlutoLog
import com.mocklets.pluto.modules.customactions.CustomAction
import com.mocklets.pluto.modules.customactions.OnCustomActionListener
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
            Log.d("exception", "uncaught exception handled on thread: " + thread.name, tr)
        }

        Pluto.setCustomActions(
            listOf(
                CustomAction(
                    title = "Show toast 1",
                    shouldClosePluto = false,
                    object : OnCustomActionListener {
                        override fun onClick() {
                            Toast.makeText(applicationContext, "Custom action 1", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                ),
                CustomAction(
                    title = "Show toast 2 and close Pluto",
                    shouldClosePluto = true,
                    object : OnCustomActionListener {
                        override fun onClick() {
                            Toast.makeText(applicationContext, "Custom action 2", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                )
            )
        )
    }
}
