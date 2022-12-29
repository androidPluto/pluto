package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.view.View
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.ImageViewParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.TextViewParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.ViewGroupParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.ViewParser

internal class AttributeParser {
    private val parsers = arrayListOf<IParser<*>>().apply {
        add(ImageViewParser())
        add(TextViewParser())
        add(ViewGroupParser())
        add(ViewParser())
    }

    fun parse(view: View): List<ParsedAttribute> {
        val attributes = arrayListOf<ParsedAttribute>()
        for (parser in parsers) {
            val attributeList = parser.getAttributes(view)
            if(!attributeList.isNullOrEmpty()) {
                attributes.add(ParsedAttribute(parser.parameterizedType, attributeList.sortedBy { it.type.title }))
            }
        }
        return attributes
    }
}
