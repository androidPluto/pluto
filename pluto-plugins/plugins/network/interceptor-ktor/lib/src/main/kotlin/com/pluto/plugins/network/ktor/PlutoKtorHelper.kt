package com.pluto.plugins.network.ktor

import androidx.annotation.Keep
import com.pluto.plugins.network.intercept.NetworkInterceptor
import com.pluto.plugins.network.ktor.internal.KtorRequestConverter.convert
import com.pluto.plugins.network.ktor.internal.KtorResponseConverter.convert
import io.ktor.client.HttpClient
import io.ktor.client.call.save
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.url
import io.ktor.util.AttributeKey
import io.ktor.utils.io.errors.IOException

private val saveAttributeKey = AttributeKey<Unit>("ResponseBodySaved")

@Deprecated("install the PlutoKtorInterceptor plugin instead")
fun HttpClient.addPlutoKtorInterceptor() {
    plugin(HttpSend).intercept { requestUnBuilt ->
        val request = requestUnBuilt.build()
        val networkInterceptor = NetworkInterceptor.intercept(request.convert(), NetworkInterceptor.Option(NAME))
        val callResult = try {
            requestUnBuilt.url(networkInterceptor.actualOrMockRequestUrl)
            execute(requestUnBuilt)
        } catch (e: IOException) {
            networkInterceptor.onError(e)
            throw e
        }
        val res = if (callResult.attributes.contains(saveAttributeKey)) {
            callResult
        } else {
            val newCall = callResult.save()
            newCall.attributes.put(saveAttributeKey, Unit)
            newCall
        }
        networkInterceptor.onResponse(res.response.convert())
        res
    }
}

@Keep
class PlutoKtorInterceptor {
    companion object : HttpClientPlugin<Unit, PlutoKtorInterceptor> {

        override val key: AttributeKey<PlutoKtorInterceptor>
            get() = AttributeKey("PlutoKtorInterceptor")

        override fun prepare(block: Unit.() -> Unit): PlutoKtorInterceptor {
            return PlutoKtorInterceptor()
        }

        override fun install(plugin: PlutoKtorInterceptor, scope: HttpClient) {
            scope.addPlutoKtorInterceptor()
        }

    }
}


private const val NAME = "Ktor"

