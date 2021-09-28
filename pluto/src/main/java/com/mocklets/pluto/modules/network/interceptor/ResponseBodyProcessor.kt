package com.mocklets.pluto.modules.network.interceptor

import android.content.Context
import com.mocklets.pluto.modules.network.ApiCallData
import com.mocklets.pluto.modules.network.NetworkCallsRepo
import com.mocklets.pluto.modules.network.convertPretty
import java.io.File
import java.io.IOException
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.GzipSource
import okio.Source
import okio.buffer
import okio.source

internal class ResponseBodyProcessor(context: Context) {
    private val maxContentLength = MAX_CONTENT_LENGTH
    private val cacheDirectoryProvider = CacheDirectoryProvider { context.filesDir }

    fun processBody(response: Response, apiCallData: ApiCallData): Response {
        apiCallData.response = response.convert(null)
        NetworkCallsRepo.set(apiCallData)
        val responseBody = response.body()
        if (!response.hasBody() || responseBody == null) {
            return response
        }

        val contentType = responseBody.contentType()
        val contentLength = responseBody.contentLength()

        apiCallData.hasResponseBody = true
        NetworkCallsRepo.set(apiCallData)

        val sideStream = ReportingSink(
            createTempTransactionFile(),
            ApiCallReportingSinkCallback(response, apiCallData),
            maxContentLength
        )
        var upstream: Source = TeeSource(responseBody.source(), sideStream)
        upstream = DepletingSource(upstream)

        return response.newBuilder()
            .body(upstream.buffer().asResponseBody(contentType, contentLength))
            .build()
    }

    private fun createTempTransactionFile(): File? {
        val cache = cacheDirectoryProvider.provide()
        return if (cache == null) {
            IOException("Failed to obtain a valid cache directory for Pluto transaction file").printStackTrace()
            null
        } else {
            FileFactory.create(cache)
        }
    }

    private inner class ApiCallReportingSinkCallback(
        private val response: Response,
        private val apiCallData: ApiCallData
    ) : ReportingSink.Callback {

        override fun onSuccess(file: File?, sourceByteCount: Long) {
            file?.let { f ->
                readResponseBuffer(f, response.isGzipped)?.let {
                    val responseBody = response.body() ?: return
                    val body = responseBody.convertPretty(it)
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
    }

    companion object {
        private const val MAX_CONTENT_LENGTH = 300_000L
//        private const val MAX_BLOB_SIZE = 1_000_000L
    }
}

/** Returns a new response body that transmits this source. */
fun BufferedSource.asResponseBody(contentType: MediaType? = null, contentLength: Long = -1L) = object : ResponseBody() {
    override fun contentType() = contentType

    override fun contentLength() = contentLength

    override fun source() = this@asResponseBody
}
