package com.pluto.plugins

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.pluto.plugins.utilities.DebugLog
import com.pluto.plugins.utilities.extensions.toast
import com.pluto.plugins.utilities.list.ListItem

@Keep
abstract class Plugin(val devIdentifier: String) : ListItem() {

    val context: Context
        get() = returnContext()
    private var _application: Application? = null
    private fun returnContext(): Context {
        _application?.let {
            return it.applicationContext
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    var savedInstance: Bundle = Bundle()
        private set

    fun install(application: Application) {
        this._application = application
        onPluginInstalled()
    }

    abstract fun getConfig(): PluginConfiguration
    abstract fun getView(): Fragment
    open fun getDeveloperDetails(): DeveloperDetails? = null
    open fun shouldInstallPlugin(): Boolean = true

    /**
     * plugin lifecycle methods
     */
    abstract fun onPluginInstalled()
    abstract fun onPluginDataCleared()

    fun onPluginViewCreated(savedInstanceState: Bundle?) {
        DebugLog.d("pluto_plugin", "view switched :: ${getConfig().name} : $savedInstanceState")
        context.toast("View switched to ${getConfig().name}")
    }

    @Deprecated("global level plugin options are no longer supported")
    fun getOptions(): List<PluginOption> = emptyList()

    override fun equals(other: Any?): Boolean = other is Plugin && devIdentifier == other.devIdentifier
    override fun hashCode(): Int = devIdentifier.hashCode()
}
