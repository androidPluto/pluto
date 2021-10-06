package com.mocklets.pluto

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.mocklets.pluto.core.Session
import com.mocklets.pluto.core.preferences.Preferences
import com.mocklets.pluto.modules.activities.ActivityTracker
import com.mocklets.pluto.modules.customactions.CustomAction
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
    private var crashHandler: CrashHandler? = null
    internal var customActions: MutableList<CustomAction> = mutableListOf()

    @JvmOverloads
    fun initialize(context: Context, shouldShowIntroToast: Boolean = true) {
        if (appContext != null) {
            return
        }

        session = Session()
        appContext = context.applicationContext
        preferences = Preferences(context)
        NetworkProxyRepo.init(context)
        crashHandler = CrashHandler(context)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
        exceptionRepo = ExceptionRepo(context)
        activity = ActivityTracker(context.applicationContext as Application, shouldShowIntroToast)
        anrSupervisor = AnrSupervisor()

        anrSupervisor.start()
    }

    fun setAppProperties(properties: HashMap<String, String?>) {
        this.appProperties.putAll(properties)
    }

    fun setExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {
        this.crashHandler?.let {
            it.setExceptionHandler(uncaughtExceptionHandler)
            return
        }
        throw IllegalStateException("UncaughtExceptionHandler cannot be set as Pluto is not initialised.")
    }

    fun setANRListener(listener: ANRListener) {
        anrSupervisor.setListener(listener)
    }

    fun setCustomActions(items: List<CustomAction>) {
        this.customActions.addAll(items)
    }
}
