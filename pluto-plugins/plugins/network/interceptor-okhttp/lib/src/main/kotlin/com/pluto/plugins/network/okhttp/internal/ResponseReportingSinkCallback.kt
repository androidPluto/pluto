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