package com.pluto.plugins

import java.lang.IllegalStateException

class PluginUiBridge private constructor(val bridgeComponents: UiBridgeComponents) {
    companion object {
        val get: PluginUiBridge
            get() {
                if (instance != null) {
                    return instance!!
                }
                throw IllegalStateException("PluginUiBridge not initialised yet")
            }

        private var instance: PluginUiBridge? = null

        fun create(bridgeComponents: UiBridgeComponents): PluginUiBridge? {
            if (instance == null) {
                synchronized(PluginUiBridge::class.java) {
                    if (instance == null) {
                        instance = PluginUiBridge(bridgeComponents)
                    }
                }
            }
            return instance
        }
    }
}
