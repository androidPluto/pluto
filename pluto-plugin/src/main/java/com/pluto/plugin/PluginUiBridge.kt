package com.pluto.plugin

object PluginUiBridge {

    var bridgeComponents: UiBridgeComponents? = null

    internal fun initialize(bridgeComponents: UiBridgeComponents) {
        this.bridgeComponents = bridgeComponents
    }
}
