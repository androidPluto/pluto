package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.widget.ImageView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttributeTag
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser

internal class ImageViewParser : IParser<ImageView>() {

    override fun getTypeAttributes(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        (view as ImageView).apply {
            val scaleTypeAttribute = Attribute("scale_type", scaleType.name, parameterizedTypeString, AttributeTag.ScaleType)
            attributes.add(scaleTypeAttribute)
        }
        return attributes
    }
}
