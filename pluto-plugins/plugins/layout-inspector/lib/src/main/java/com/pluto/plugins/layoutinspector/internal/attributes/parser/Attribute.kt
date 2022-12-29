package com.pluto.plugins.layoutinspector.internal.attributes.parser

import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeType
import com.pluto.utilities.list.ListItem

internal data class Attribute<T>(val type: AttributeType<T>, val value: T, val parameterizedType: String) : ListItem() {
    val valueString: CharSequence? = type.serialise(value)
}

internal data class ParsedAttribute(
    val parameterizedType: String,
    val attributes: List<Attribute<*>>
)

internal data class LayoutParamDimens(
    val layoutParam: Int,
    val size: Int
)
