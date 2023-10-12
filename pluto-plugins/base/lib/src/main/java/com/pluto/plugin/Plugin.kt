package com.pluto.plugin

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.Keep
import androidx.fragment.app.Fragment

@Keep
abstract class Plugin(val identifier: String) : PluginEntity(identifier) {

    val context: Context
        get() = returnContext()
    val application: Application
        get() = returnApplication()

    private var _application: Application? = null
    private fun returnContext(): Context {
        _application?.let {
            return it.applicationContext
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    private fun returnApplication(): Application {
        _application?.let {
            return it
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    var savedInstance: Bundle = Bundle()
        private set

    final override fun install(application: Application) {
        this._application = application
        onPluginInstalled()
    }

    abstract fun getConfig(): PluginConfiguration
    abstract fun getView(): Fragment
    open fun getDeveloperDetails(): DeveloperDetails? = null

    /**
     * plugin lifecycle methods
     */
    abstract fun onPluginInstalled()
    abstract fun onPluginDataCleared()

    @SuppressWarnings("UnusedPrivateMember")
    fun onPluginViewCreated(savedInstanceState: Bundle?) {
        Toast.makeText(context, "View switched to ${getConfig().name}", LENGTH_SHORT).show()
    }
}
