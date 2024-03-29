package com.pluto.plugins.layoutinspector.internal.attributes.data.parser

import android.view.View
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types.ImageViewParser
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types.TextViewParser
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types.ViewGroupParser
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types.ViewParser

internal class AttributeParser {
    private val parsers = arrayListOf<IParser<*>>().apply {
        add(ImageViewParser())
        add(TextViewParser())
        add(ViewGroupParser())
        add(ViewParser())
    }

    fun parse(view: View): List<ParsedAttribute> {
        val attributes = arrayListOf<ParsedAttribute>()
        parsers.forEach { parser ->
            val attributeList = parser.getAttributes(view)
            if (!attributeList.isNullOrEmpty()) {
                attributes.add(ParsedAttribute(parser.parameterizedType, attributeList.sortedBy { it.title }))
            }
        }
        return attributes
    }

    data class ParsedAttribute(
        val parameterizedType: String,
        val attributes: List<Attribute<*>>
    )
}
