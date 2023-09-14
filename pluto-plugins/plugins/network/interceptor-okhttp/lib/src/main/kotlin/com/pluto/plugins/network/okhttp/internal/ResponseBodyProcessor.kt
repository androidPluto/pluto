package com.pluto.plugins.network.okhttp.internal

import com.pluto.plugin.libinterface.PlutoInterface
import com.pluto.plugins.network.intercept.NetworkData
import com.pluto.plugins.network.okhttp.internal.utilities.DepletingSource
import com.pluto.plugins.network.okhttp.internal.utilities.ReportingSink
import com.pluto.plugins.network.okhttp.internal.utilities.TeeSource
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.GzipSource
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException

internal object ResponseBodyProcessor {

    fun processBody(response: Response, onComplete: (NetworkData.Response) -> Unit): Response {
        val responseBody = response.body
        if (!response.hasBody() || responseBody == null) {
            onComplete.invoke(response.convert(null))
            return response
        }
        val sideStream = ReportingSink(PlutoInterface.files.createFile(), ApiCallReportingSinkCallback(response, onComplete))
        val processedResponseBody: ResponseBody = DepletingSource(TeeSource(responseBody.source(), sideStream))
            .buffer()
            .asResponseBody(responseBody)

        return response.newBuilder()
            .body(processedResponseBody)
            .build()
    }

    private class ApiCallReportingSinkCallback(
        private val response: Response,
        private val onComplete: (NetworkData.Response) -> Unit
    ) : ReportingSink.Callback {

        override fun onSuccess(file: File?, sourceByteCount: Long) {
            file?.let { f ->
                readResponseBuffer(f, response.isGzipped)?.let {
                    val responseBody = response.body ?: return
                    val body = responseBody.processBody(it)
                    onComplete.invoke(response.convert(body))
                }
                f.delete()
            }
        }

        override fun onFailure(file: File?, exception: IOException) = exception.printStackTrace()

        private fun readResponseBuffer(responseBody: File, isGzipped: Boolean) = try {
            val bufferedSource = responseBody.source().buffer()
            val source = if (isGzipped) {
                GzipSource(bufferedSource)
            } else {
                bufferedSource
            }
            Buffer().apply { source.use { writeAll(it) } }
        } catch (e: IOException) {
            IOException("Response payload couldn't be processed by Pluto", e).printStackTrace()
            null
        }
    }
}

/** Returns a new response body that transmits this source. */
private fun BufferedSource.asResponseBody(referenceBody: ResponseBody) = object : ResponseBody() {
    override fun contentType() = referenceBody.contentType()

    override fun contentLength() = referenceBody.contentLength()

    override fun source() = this@asResponseBody
}
