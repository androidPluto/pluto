package com.sampleapp.functions.network.internal.core

import com.pluto.plugins.network.addPlutoKtorPlugin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

val Client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}.apply {
    addPlutoKtorPlugin()
}
