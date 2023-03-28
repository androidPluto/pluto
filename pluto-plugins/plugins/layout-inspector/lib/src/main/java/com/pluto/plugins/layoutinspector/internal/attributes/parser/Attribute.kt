package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeType
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.list.ListItem

internal data class Attribute<T>(val type: AttributeType<T>, val value: T) : ListItem() {
    val valueString: CharSequence? = type.serialise(value)
}

internal data class ParsedAttribute(
    val parameterizedType: String,
    val attributes: List<Attribute<*>>
)

internal data class LayoutParamDimens(
    val layoutParam: Int,
    val size: Int
) {
    override fun toString(): String {
        val dp = "${size.toFloat().px2dp.toInt()} dp"
        return when (layoutParam) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> "wrap_content ($dp)"
            ViewGroup.LayoutParams.MATCH_PARENT -> "match_parent ($dp)"
            else -> dp
        }
    }
}
