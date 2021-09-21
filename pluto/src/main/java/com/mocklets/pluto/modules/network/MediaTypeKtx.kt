package com.mocklets.pluto.modules.network

import com.mocklets.pluto.modules.network.transformers.FormEncodedTransformer
import com.mocklets.pluto.modules.network.transformers.JsonBaseTransformer
import com.mocklets.pluto.modules.network.transformers.XmlBaseTransformer
import okhttp3.MediaType

internal fun MediaType.isText(): Boolean {
    return (type() == "application" || type() == "text") &&
        (subtype().endsWith("json") || subtype() == "plain" || subtype() == "xml" || subtype() == "html" || subtype() == "x-www-form-urlencoded")
}

internal fun MediaType.beautify(plain: CharSequence, indent: Int = BODY_INDENTATION): CharSequence? {
    return when {
        subtype().endsWith("json") -> JsonBaseTransformer().beautify(plain, indent)
        subtype() == "xml" || subtype() == "html" -> XmlBaseTransformer().beautify(plain, indent)
        subtype() == "x-www-form-urlencoded" -> FormEncodedTransformer().beautify(plain)
        else -> plain
    }
}
