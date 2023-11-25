package com.pluto.plugins.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.util.AttributeKey

@Deprecated("install the PlutoKtorInterceptor plugin instead")
@SuppressWarnings("EmptyFunctionBlock")
fun HttpClient.addPlutoKtorInterceptor() {
}

class PlutoKtorInterceptor private constructor() {
    companion object : HttpClientPlugin<Unit, PlutoKtorInterceptor> {
        override val key: AttributeKey<PlutoKtorInterceptor>
            get() = AttributeKey("PlutoKtorInterceptor")

        override fun prepare(block: Unit.() -> Unit): PlutoKtorInterceptor {
            return PlutoKtorInterceptor()
        }

        override fun install(plugin: PlutoKtorInterceptor, scope: HttpClient) {
            // no-op
        }
    }
}
