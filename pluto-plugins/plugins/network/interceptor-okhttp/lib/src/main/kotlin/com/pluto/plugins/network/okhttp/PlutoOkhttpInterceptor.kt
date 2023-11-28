package com.pluto.plugins.network.okhttp

import androidx.annotation.Keep
import com.pluto.plugin.libinterface.PlutoInterface
import com.pluto.plugins.network.intercept.NetworkData
import com.pluto.plugins.network.intercept.NetworkInterceptor
import com.pluto.plugins.network.okhttp.internal.ResponseReportingSinkCallback
import com.pluto.plugins.network.okhttp.internal.convert
import com.pluto.plugins.network.okhttp.internal.hasBody
import com.pluto.plugins.network.okhttp.internal.utilities.DepletingSource
import com.pluto.plugins.network.okhttp.internal.utilities.ReportingSink
import com.pluto.plugins.network.okhttp.internal.utilities.TeeSource
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.buffer
import java.io.IOException

@Keep
class PlutoOkhttpInterceptor {
    companion object : Interceptor {
        private const val NAME = "Okhttp"

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val networkInterceptor = NetworkInterceptor.intercept(request.convert(), NetworkInterceptor.Option(NAME))
            val response: Response = try {
                val builder = request.newBuilder().url(networkInterceptor.actualOrMockRequestUrl)
                chain.proceed(builder.build())
            } catch (e: IOException) {
                networkInterceptor.onError(e)
                throw e
            }
            return response.processBody { networkInterceptor.onResponse(it) }
        }
    }
}

private fun Response.processBody(onComplete: (NetworkData.Response) -> Unit): Response {
    if (!hasBody()) {
        onComplete.invoke(convert(null))
        return this
    }
    val responseBody: ResponseBody = body as ResponseBody
    val sideStream = ReportingSink(PlutoInterface.files.createFile(), ResponseReportingSinkCallback(this, onComplete))
    val processedResponseBody: ResponseBody = DepletingSource(TeeSource(responseBody.source(), sideStream))
        .buffer()
        .asResponseBody(responseBody)

    return newBuilder()
        .body(processedResponseBody)
        .build()
}

/** Returns a new response body that transmits this source. */
private fun BufferedSource.asResponseBody(referenceBody: ResponseBody) = object : ResponseBody() {
    override fun contentType() = referenceBody.contentType()

    override fun contentLength() = referenceBody.contentLength()

    override fun source() = this@asResponseBody
}

