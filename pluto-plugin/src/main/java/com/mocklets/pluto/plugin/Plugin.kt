package com.mocklets.pluto.plugin

import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.mocklets.pluto.utilities.list.ListItem

@Keep
abstract class Plugin : ListItem() {

    val context: Context
        get() = returnContext()
    private var _context: Context? = null
    private fun returnContext(): Context {
        _context?.let {
            return it
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    var savedInstance: Bundle = Bundle()
        private set

    fun install(context: Context) {
        this._context = context
        onPluginInstalled(context)
    }

    abstract fun getConfig(): PluginConfiguration
    abstract fun getView(): Fragment
    open fun getDeveloperDetails(): DeveloperDetails? = null
    open fun shouldInstallPlugin(): Boolean = true
    open fun saveInstanceState(state: Bundle) {
        savedInstance = state
    }

    /**
     * plugin lifecycle methods
     */
    abstract fun onPluginInstalled(context: Context)
    open fun onPluginViewCreated(savedInstanceState: Bundle?) {}
    open fun onPluginViewRemoved() {}
    open fun onPluginSearchInitiated(search: String) {}
    open fun onPluginOptionsSelected(option: String) {}

    override fun equals(other: Any?): Boolean = other is Plugin && getConfig().hashCode() == other.getConfig().hashCode()
    override fun hashCode(): Int = getConfig().hashCode()
}
