package com.mocklets.pluto.network.internal.interceptor

import com.mocklets.pluto.network.internal.ProcessedBody
import com.mocklets.pluto.network.internal.RequestData
import com.mocklets.pluto.network.internal.ResponseData
import com.mocklets.pluto.network.internal.Status
import com.mocklets.pluto.network.internal.convertPretty
import com.mocklets.pluto.plugin.utilities.DebugLog
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream
import okhttp3.Request
import okhttp3.Response
import okio.IOException

internal fun Request.convert(): RequestData {
    return RequestData(
        url = this.url(),
        method = this.method(),
        body = this.body()?.convertPretty(this.isGzipped),
        headers = this.headerMap(),
        timestamp = System.currentTimeMillis(),
        isGzipped = this.isGzipped
    )
}

internal fun Request.headerMap(): Map<String, String?> {
    val headerNames = arrayListOf<String>()
    headerNames.addAll(headers().names())
    headerNames.add("content-type")
//    headerNames.add("content-length") todo get content-length header
    headerNames.sortBy { it }

    val map = mutableMapOf<String, String?>()
    headerNames.forEach {
        val key = it.lowercase().trim()
        when (it) {
            "content-type" -> body()?.contentType()?.toString()?.let { value ->
                map[key] = value.trim()
            }
            else -> map[key] = headers().get(it)?.trim()
        }
    }
    return map
}

internal fun Response.convert(body: ProcessedBody?): ResponseData {
    return ResponseData(
        status = Status(code(), statusCodeMessage()),
        isSuccessful = isSuccessful,
        body = body,
        protocol = protocol(),
        fromDiskCache = false,
        headers = headersMap(),
        sendTimeMillis = sentRequestAtMillis(),
        receiveTimeMillis = receivedResponseAtMillis(),
        isGzipped = isGzipped
    )
}

private fun Response.statusCodeMessage(): String {
    return message() // TODO compile local message if server message is blank
}

private fun Response.headersMap(): Map<String, String?> {
    val headerNames = arrayListOf<String>()
    headerNames.addAll(headers().names())
    headerNames.sortBy { it }

    val map = mutableMapOf<String, String?>()
    headerNames.forEach {
        map[it.lowercase().trim()] = headers().get(it)?.trim()
    }

    return map
}

@Suppress("TooGenericExceptionCaught")
internal fun doUnZipToString(gzippedMessage: ByteArray?): String {
    var unzippedMessage: String? = null
    try {
        val gzippped = doUnZip(gzippedMessage)
        unzippedMessage = String(gzippped!!, Charset.defaultCharset())
    } catch (e: Throwable) {
        DebugLog.e(LOGTAG, "doUnZipToString 1", e)
    }
    return unzippedMessage ?: ""
}

private fun doUnZip(stream: InputStream?): ByteArray? {
    if (stream !is ByteArrayInputStream) {
        return try {
            doUnZip(stream?.readBytes())
        } catch (e: IOException) {
            DebugLog.e(LOGTAG, "doUnZip 1", e)
            null
//            throw SystemFailedException(e.getMessage(), e)
        }
    }
    var bos: ByteArrayOutputStream? = null
    var gzipStream: InputStream? = null
    var bytes: ByteArray? = null
    try {
        bos = ByteArrayOutputStream()
        gzipStream = GZIPInputStream(stream)
        copy(gzipStream, bos)
        bytes = bos.toByteArray()
    } catch (e: IOException) {
        DebugLog.e(LOGTAG, "error while unzip", e)
    } finally {
        try {
            gzipStream?.close()
            bos?.close()
        } catch (e: IOException) {
            DebugLog.e(LOGTAG, "error while closing stream", e)
        }
    }
    return bytes
}

private fun doUnZip(zippedMessage: ByteArray?): ByteArray? {
    var stream: ByteArrayInputStream? = null
    return try {
        stream = ByteArrayInputStream(zippedMessage)
        doUnZip(stream)
    } finally {
        try {
            stream?.close()
        } catch (e: IOException) {
            DebugLog.e(LOGTAG, "error while closing zippedMessage stream", e)
        }
    }
}

private const val BUFFER_SIZE = 1024

@Throws(IOException::class)
private fun copy(stream: InputStream, out: OutputStream) {
    val buf = ByteArray(BUFFER_SIZE)
    var len: Int
    while (stream.read(buf, 0, buf.size).also { len = it } != -1) {
        out.write(buf, 0, len)
    }
}

private const val LOGTAG = "data-convertor"
