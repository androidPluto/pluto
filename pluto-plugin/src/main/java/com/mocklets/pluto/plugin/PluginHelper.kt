package com.mocklets.pluto.plugin

object PluginHelper {
    internal var stateUpdater: PlutoStateUpdater? = null

    fun closeScreen() {
        stateUpdater?.onCloseRequested()
    }
}
