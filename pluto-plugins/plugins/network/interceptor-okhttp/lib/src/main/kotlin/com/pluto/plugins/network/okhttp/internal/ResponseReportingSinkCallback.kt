package com.pluto.plugins.network.okhttp.internal

import com.pluto.plugins.network.intercept.NetworkData
import com.pluto.plugins.network.okhttp.internal.utilities.ReportingSink
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException

class ResponseReportingSinkCallback(
    private val response: Response,
    private val onComplete: (NetworkData.Response) -> Unit
) : ReportingSink.Callback {

    override fun onSuccess(file: File?, sourceByteCount: Long) {
        val buffer = file?.let { readResponseBuffer(it, response.isGzipped) }
        val body = buffer?.let { response.body.processBody(it) }
        file?.delete()
        onComplete.invoke(response.convert(body))
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