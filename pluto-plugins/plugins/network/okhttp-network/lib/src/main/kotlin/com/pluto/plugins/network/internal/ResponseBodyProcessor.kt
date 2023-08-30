package com.pluto.plugins.network.internal

import com.pluto.plugins.network.internal.interceptor.logic.ResponseData
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.GzipSource
import okio.Source
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException

internal object ResponseBodyProcessor {
    private const val MAX_CONTENT_LENGTH = 300_000L
    private const val maxContentLength = MAX_CONTENT_LENGTH

    fun processBody(cacheDirectoryProvider: CacheDirectoryProvider, response: Response, onComplete: (ResponseData) -> Unit): Response {
        onComplete.invoke(response.convert(null))
        val responseBody = response.body
        if (!response.hasBody() || responseBody == null) {
            return response
        }

        val contentType = responseBody.contentType()
        val contentLength = responseBody.contentLength()

//        todo check this, if hasResponseBody needed or not
//        apiCallData.hasResponseBody = true
//        NetworkCallsRepo.set(apiCallData)

        val sideStream = ReportingSink(
            createTempTransactionFile(cacheDirectoryProvider),
            ApiCallReportingSinkCallback(response, onComplete),
            maxContentLength
        )
        var upstream: Source = TeeSource(responseBody.source(), sideStream)
        upstream = DepletingSource(upstream)

        return response.newBuilder()
            .body(upstream.buffer().asResponseBody(contentType, contentLength))
            .build()
    }

    private fun createTempTransactionFile(cacheDirectoryProvider: CacheDirectoryProvider): File? {
        val cache = cacheDirectoryProvider.provide()
        return if (cache == null) {
            IOException("Failed to obtain a valid cache directory for Pluto transaction file").printStackTrace()
            null
        } else {
            FileFactory.create(cache)
        }
    }

    private class ApiCallReportingSinkCallback(
        private val response: Response,
        private val onComplete: (ResponseData) -> Unit
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
    } //        private const val MAX_BLOB_SIZE = 1_000_000L
}

/** Returns a new response body that transmits this source. */
private fun BufferedSource.asResponseBody(contentType: MediaType? = null, contentLength: Long = -1L) = object : ResponseBody() {
    override fun contentType() = contentType

    override fun contentLength() = contentLength

    override fun source() = this@asResponseBody
}
