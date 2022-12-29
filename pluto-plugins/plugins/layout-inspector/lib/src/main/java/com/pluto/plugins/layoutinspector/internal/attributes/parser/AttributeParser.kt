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

    fun parse(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        for (parser in parsers) {
            parser.getAttributes(view)?.let { attributes.addAll(it) }
        }
        return attributes
    }
}
