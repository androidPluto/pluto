package com.pluto.plugins.layoutinspector.internal.attributes.data

import android.view.ViewGroup
import com.pluto.utilities.extensions.px2dp

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

    fun actualValue(): String = when (layoutParam) {
        ViewGroup.LayoutParams.WRAP_CONTENT -> "wrap_content"
        ViewGroup.LayoutParams.MATCH_PARENT -> "match_parent"
        else -> "${size.toFloat().px2dp.toInt()}"
    }
}
