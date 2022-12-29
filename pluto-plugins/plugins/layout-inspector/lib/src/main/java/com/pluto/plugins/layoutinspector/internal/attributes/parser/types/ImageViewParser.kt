package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.widget.ImageView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeScaleType

internal class ImageViewParser : IParser<ImageView>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as ImageView).apply {
            attributes.add(Attribute(AttributeTypeScaleType("scale_type"), scaleType, parameterizedTypeString))
        }
        return attributes
    }
}
