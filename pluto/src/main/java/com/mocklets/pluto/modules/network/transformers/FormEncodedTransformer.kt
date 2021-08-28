package com.mocklets.pluto.modules.network.transformers

import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.modules.network.LOGTAG
import com.mocklets.pluto.modules.network.UTF8
import java.net.URLDecoder

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

    override fun flatten(plain: CharSequence): String? {
        return plain.toString()
    }
}
