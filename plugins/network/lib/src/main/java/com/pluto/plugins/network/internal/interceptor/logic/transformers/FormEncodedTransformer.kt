package com.pluto.plugins.network.internal.interceptor.logic.transformers

import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugins.network.internal.interceptor.logic.LOGTAG
import com.pluto.plugins.network.internal.interceptor.logic.UTF8
import java.net.URLDecoder
import java.net.URLEncoder

internal class FormEncodedTransformer : BaseTransformer {

    @Suppress("TooGenericExceptionCaught")
    override fun beautify(plain: CharSequence, indent: Int): CharSequence? {
        return try {
            val items = plain.split("&")
            val stringBuilder = StringBuilder()
            items.forEachIndexed { index, data ->
                val pair = data.split("=")
                stringBuilder.append("${pair[0]} = ${URLDecoder.decode(pair[1], UTF8.toString())}")
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

    @Suppress("TooGenericExceptionCaught")
    override fun flatten(plain: CharSequence): String {
        return try {
            val items = plain.split("\t\t&\n")
            val stringBuilder = StringBuilder()
            items.forEachIndexed { index, data ->
                val pair = data.split(" = ")
                stringBuilder.append("${pair[0]}=${URLEncoder.encode(pair[1], UTF8.toString())}")
                if (index < items.size - 1) {
                    stringBuilder.append("&")
                }
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            DebugLog.e(LOGTAG, "error while beautifying form url encoded body", e)
            plain.toString()
        }
    }
}
