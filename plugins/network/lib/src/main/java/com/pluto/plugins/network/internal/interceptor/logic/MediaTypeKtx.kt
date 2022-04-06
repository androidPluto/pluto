package com.pluto.plugins.network.internal.interceptor.logic

import com.pluto.plugins.network.internal.interceptor.logic.transformers.FormEncodedTransformer
import com.pluto.plugins.network.internal.interceptor.logic.transformers.JsonBaseTransformer
import com.pluto.plugins.network.internal.interceptor.logic.transformers.XmlBaseTransformer
import okhttp3.MediaType

internal fun MediaType.isText(): Boolean {
    return (type == "application" || type == "text") &&
        (subtype.endsWith("json") || subtype == "plain" || subtype == "xml" || subtype == "html" || subtype == "x-www-form-urlencoded")
}

internal fun MediaType.beautify(plain: CharSequence, indent: Int = BODY_INDENTATION): CharSequence? {
    return when {
        subtype.endsWith("json") -> JsonBaseTransformer().beautify(plain, indent)
        subtype == "xml" || subtype == "html" -> XmlBaseTransformer().beautify(plain, indent)
        subtype == "x-www-form-urlencoded" -> FormEncodedTransformer().beautify(plain)
        else -> plain
    }
}
