package com.pluto.plugin

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.pluto.plugin.utilities.list.ListItem

@Keep
abstract class Plugin : ListItem() {

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
    abstract fun getOptions(): List<PluginOption>
    open fun getDeveloperDetails(): DeveloperDetails? = null
    open fun shouldInstallPlugin(): Boolean = true
    open fun saveInstanceState(state: Bundle) {
        savedInstance = state
    }

    /**
     * plugin lifecycle methods
     */
    abstract fun onPluginInstalled()
    open fun onPluginViewCreated(savedInstanceState: Bundle?) {}
    abstract fun onPluginDataCleared()

    override fun equals(other: Any?): Boolean = other is Plugin && getConfig().hashCode() == other.getConfig().hashCode()
    override fun hashCode(): Int = getConfig().hashCode()
}
