package com.mocklets.pluto.plugin

import android.content.Context
import androidx.annotation.Keep

@Keep
abstract class Plugin {

    val context: Context
        get() = returnContext()

    fun install(context: Context) {
        this._context = context
        onPluginInstalled()
    }

    private var _context: Context? = null
    private fun returnContext(): Context {
        _context?.let {
            return it
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    abstract fun getConfig(): PluginConfiguration

    abstract fun getDeveloperDeveloperDetails(): DeveloperDetails

    abstract fun onPluginInstalled()

    open fun shouldInstallPlugin(): Boolean = true

    override fun equals(other: Any?): Boolean {
        return other is Plugin && getConfig().hashCode() == other.getConfig().hashCode()
    }

    override fun hashCode(): Int {
        return getConfig().hashCode()
    }
}
