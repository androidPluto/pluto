package com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types

import android.view.View
import android.widget.ImageView
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeScaleType
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.IParser

internal class ImageViewParser : IParser<ImageView>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as ImageView).apply {
            attributes.add(AttributeScaleType("scale_type", scaleType))
        }
        return attributes
    }
}
