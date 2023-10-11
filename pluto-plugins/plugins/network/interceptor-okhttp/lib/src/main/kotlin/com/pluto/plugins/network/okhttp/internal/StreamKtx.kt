package com.pluto.plugins.network.okhttp.internal

import com.pluto.utilities.DebugLog
import okio.IOException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream

@Suppress("TooGenericExceptionCaught")
internal fun ByteArray.unzipToString(): String {
    return try {
        String(this.unzip()!!, Charset.defaultCharset())
    } catch (e: Throwable) {
        com.pluto.utilities.DebugLog.e(LOG_TAG, "ByteArray.unzipToString", e)
        ""
    }
}

private fun InputStream?.unzip(): ByteArray? {
    if (this !is ByteArrayInputStream) {
        return try {
            this?.readBytes().unzip()
        } catch (e: IOException) {
            com.pluto.utilities.DebugLog.e(LOG_TAG, "doUnZip 1", e)
            null
        }
    }
    var bos: ByteArrayOutputStream? = null
    var gzipStream: InputStream? = null
    var bytes: ByteArray? = null
    try {
        bos = ByteArrayOutputStream()
        gzipStream = GZIPInputStream(this)
        gzipStream.copyTo(bos)
        bytes = bos.toByteArray()
    } catch (e: IOException) {
        com.pluto.utilities.DebugLog.e(LOG_TAG, "error while unzip", e)
    } finally {
        try {
            gzipStream?.close()
            bos?.close()
        } catch (e: IOException) {
            com.pluto.utilities.DebugLog.e(LOG_TAG, "error while closing stream", e)
        }
    }
    return bytes
}

private fun ByteArray?.unzip(): ByteArray? {
    var stream: ByteArrayInputStream? = null
    return try {
        stream = ByteArrayInputStream(this)
        stream.unzip()
    } finally {
        try {
            stream?.close()
        } catch (e: IOException) {
            com.pluto.utilities.DebugLog.e(LOG_TAG, "error while closing zippedMessage stream", e)
        }
    }
}

private const val LOG_TAG = "stream"
