package com.pluto.plugins.network.okhttp

import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.core.CacheDirectoryProvider
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
    val maxContentLength = MAX_CONTENT_LENGTH

    fun processBody(cacheDirectoryProvider: CacheDirectoryProvider, response: Response, apiCallData: ApiCallData): Response {
        apiCallData.response = response.convert(null)
        NetworkCallsRepo.set(apiCallData)
        val responseBody = response.body
        if (!response.hasBody() || responseBody == null) {
            return response
        }

        val contentType = responseBody.contentType()
        val contentLength = responseBody.contentLength()

        apiCallData.hasResponseBody = true
        NetworkCallsRepo.set(apiCallData)

        val sideStream = ReportingSink(
            createTempTransactionFile(cacheDirectoryProvider),
            ApiCallReportingSinkCallback(response, apiCallData),
            maxContentLength
        )
        var upstream: Source = TeeSource(responseBody.source(), sideStream)
        upstream = DepletingSource(upstream)

        return response.newBuilder()
            .body(upstream.buffer().asResponseBody(contentType, contentLength))
            .build()
    }

    fun createTempTransactionFile(cacheDirectoryProvider: CacheDirectoryProvider): File? {
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
        private val apiCallData: ApiCallData
    ) : ReportingSink.Callback {

        override fun onSuccess(file: File?, sourceByteCount: Long) {
            file?.let { f ->
                readResponseBuffer(f, response.isGzipped)?.let {
                    val responseBody = response.body ?: return
                    val body = responseBody.processBody(it)
                    apiCallData.response = response.convert(body)
                    NetworkCallsRepo.set(apiCallData)
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
