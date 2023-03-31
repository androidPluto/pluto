package com.pluto.plugins.layoutinspector.internal.attributes.data

internal data class ParsedAttribute(
    val parameterizedType: String,
    val attributes: List<Attribute<*>>
)
