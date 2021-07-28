package com.mocklets.pluto.modules.network

import android.content.Context
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.ui.spannable.createSpan
import com.mocklets.pluto.modules.network.interceptor.doUnZipToString
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xml.sax.InputSource

internal fun MediaType.isText(): Boolean {
    return (type() == "application" || type() == "text") &&
        (subtype() == "json" || subtype() == "plain" || subtype() == "xml" || subtype() == "html" || subtype() == "x-www-form-urlencoded")
}

internal fun RequestBody.convertPretty(gzipped: Boolean): ProcessedBody {
    contentType()?.let {
        DebugLog.e(LOGTAG, "request : ${it.type()}, ${it.subtype()}, ${it.charset()}")
        return if (it.isText()) {
            val plainBody = convert(gzipped)
            ProcessedBody(
                isValid = true,
                body = it.beautify(plainBody)
            )
        } else {
            ProcessedBody(
                isValid = true,
                body = BINARY_BODY,
                isBinary = true
            )
        }
    }
    return ProcessedBody(
        isValid = false
    )
}

private fun MediaType.beautify(plain: CharSequence, indent: Int = BODY_INDENTATION): CharSequence? {
    return when (subtype()) {
        "json" -> beautifyJson(plain, indent)
        "xml", "html" -> beautifyXml(plain, indent)
        "x-www-form-urlencoded" -> beautifyFormUrlEncoding(plain)
//        "html" -> beautifyHtml(plain, indent)
        else -> plain
    }
}

internal fun ResponseBody?.convertPretty(buffer: Buffer): ProcessedBody? {
    this?.let {
        val contentType = it.contentType()
        if (contentType != null) {
            return if (contentType.isText()) {
                val body = buffer.readString(contentType.charset(UTF8) ?: UTF8)
                ProcessedBody(
                    isValid = true,
                    body = contentType.beautify(body)
                )
            } else {
                // todo process image response
                ProcessedBody(
                    isValid = true,
                    body = BINARY_BODY,
                    isBinary = true
                )
            }
        }
        return ProcessedBody(
            isValid = false
        )
    }
    return null
}

private fun RequestBody.convert(gzipped: Boolean): CharSequence {
    return try {
        val buffer = Buffer()
        writeTo(buffer)
        if (gzipped) {
            doUnZipToString(buffer.readByteArray())
        } else {
            buffer.readUtf8()
        }
    } catch (e: IOException) {
        DebugLog.e(LOGTAG, "request body parsing failed", e)
        ""
    }
}

private fun beautifyJson(plainBody: CharSequence, indent: Int = BODY_INDENTATION): CharSequence {
    return try {
        val je: JsonElement = JsonParser.parseString(plainBody.toString())
        when {
            je.isJsonArray -> JSONArray(plainBody.toString()).toString(indent)
            je.isJsonObject -> JSONObject(plainBody.toString()).toString(indent)
            else -> plainBody
        }
    } catch (e: JsonSyntaxException) {
        DebugLog.e(LOGTAG, "json parsing failed", e)
        plainBody
    } catch (e: JSONException) {
        DebugLog.e(LOGTAG, "json parsing failed", e)
        plainBody
    }
}

@Suppress("TooGenericExceptionCaught")
private fun beautifyXml(plainBody: CharSequence, indent: Int = BODY_INDENTATION): CharSequence {
    return try {
        val serializer: Transformer = SAXTransformerFactory.newInstance().newTransformer()
        serializer.setOutputProperty(OutputKeys.INDENT, "yes")
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "$indent")
        val xmlSource = SAXSource(InputSource(ByteArrayInputStream(plainBody.toString().toByteArray())))
        val res = StreamResult(ByteArrayOutputStream())
        serializer.transform(xmlSource, res)
        String((res.outputStream as ByteArrayOutputStream).toByteArray())
    } catch (e: Exception) {
        DebugLog.e(LOGTAG, "xml parsing failed", e)
        plainBody
    }
}

@Suppress("TooGenericExceptionCaught")
fun beautifyFormUrlEncoding(plain: CharSequence): CharSequence? {
    return try {
        val items = plain.split("&")
        val stringBuilder = StringBuilder()
        items.forEachIndexed { index, data ->
            val pair = data.split("=")
            stringBuilder.append("${pair[0]} = ${pair[1]}")
            if (index < items.size - 1) {
                stringBuilder.append("\t\t&\n")
            }
        }
        stringBuilder
    } catch (e: Exception) {
        DebugLog.e(LOGTAG, "error while beautifying form url encoded body", e)
        plain
    }
}

// private fun beautifyHtml(plainBody: CharSequence, indent: Int = BODY_INDENTATION): CharSequence {
//    return try {
//        val tidy = Tidy()
//        tidy.xhtml = true
//        tidy.indentContent = true
//        tidy.printBodyOnly = true
//        tidy.tidyMark = false
//        tidy.spaces = indent
//
//        val htmlDOM = tidy.parseDOM(ByteArrayInputStream(plainBody.toString().toByteArray()), null)
//        val out: OutputStream = ByteArrayOutputStream()
//        tidy.pprint(htmlDOM, out)
//
//        out.toString()
//    } catch (e: Exception) {
//        DebugLog.e("pluto_sdk", "html parsing failed", e)
//        plainBody
//    }
// }

internal fun Context?.beautifyHeaders(data: Map<String, String?>): CharSequence? {
    return this?.createSpan {
        data.forEach {
            append("${it.key} : ")
            if (it.value != null) {
                append(fontColor(semiBold("${it.value}"), context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }?.trim()
}

internal fun Context?.beautifyQueryParams(url: HttpUrl): CharSequence? {
    return this?.createSpan {
        url.queryParameterNames().forEach {
            append("$it : ")
            val value = url.queryParameter(it)
            if (value != null) {
                append(fontColor(semiBold("$value"), context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }?.trim()
}

internal fun ProcessedBody?.flatten(): String? {
    this?.let {
        it.body?.toString()?.let { body ->
            return if (it.isBinary) {
                body
            } else {
                body.replace("\n", "").replace("\\s+".toRegex(), "")
            }
        }
        return null
    }
    return null
}

internal fun String.pruneQueryParams(): String {
    val separated: List<String> = split("?")
    return separated[0]
}

internal fun HttpUrl.hostUrl(): String {
    val hostString = StringBuilder()
    hostString.append("${scheme()}://${host()}")
    if (port() != HTTP_PORT && port() != HTTPS_PORT) {
        hostString.append(":${port()}")
    }
    return hostString.toString()
}

private const val LOGTAG = "pluto_sdk"
private const val BODY_INDENTATION = 4
private const val BINARY_BODY = "~ Binary Data"
internal val UTF8 = Charset.forName("UTF-8")
private const val HTTP_PORT = 80
private const val HTTPS_PORT = 443
