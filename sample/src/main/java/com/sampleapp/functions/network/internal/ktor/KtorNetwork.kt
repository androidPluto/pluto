package com.sampleapp.functions.network.internal.ktor

import com.pluto.plugins.network.addPlutoKtorPlugin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

val Client = HttpClient {
    defaultRequest {
        header("network-type", "ktor")
        url.protocol = URLProtocol.HTTPS
        url.host = "api.mocklets.com/p68296"
    }
    install(ContentNegotiation) {
        json()
    }
}.apply {
    addPlutoKtorPlugin()
}
