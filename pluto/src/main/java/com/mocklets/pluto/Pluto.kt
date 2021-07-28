package com.mocklets.pluto

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.mocklets.pluto.core.Session
import com.mocklets.pluto.core.preferences.Preferences
import com.mocklets.pluto.modules.activities.ActivityTracker
import com.mocklets.pluto.modules.exceptions.ANRListener
import com.mocklets.pluto.modules.exceptions.ExceptionRepo
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor
import com.mocklets.pluto.modules.exceptions.crashes.CrashHandler
import com.mocklets.pluto.modules.network.proxy.NetworkProxyRepo

@Keep
object Pluto {
    internal var appContext: Context? = null
    internal lateinit var activity: ActivityTracker
    internal lateinit var exceptionRepo: ExceptionRepo
    internal lateinit var preferences: Preferences
    private lateinit var anrSupervisor: AnrSupervisor
    internal lateinit var session: Session
    internal var appProperties: HashMap<String, String?> = hashMapOf()

    fun initialize(context: Context) {
        if (appContext != null) {
            return
        }
        session = Session()
        appContext = context.applicationContext
        preferences = Preferences(context)
        NetworkProxyRepo.init(context)
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler(context))
        exceptionRepo = ExceptionRepo(context)
        activity = ActivityTracker(context.applicationContext as Application)
        anrSupervisor = AnrSupervisor()

        anrSupervisor.start()
    }

    fun setAppProperties(properties: HashMap<String, String?>) {
        this.appProperties.putAll(properties)
    }

    fun setANRListener(listener: ANRListener) {
        anrSupervisor.setListener(listener)
    }
}
