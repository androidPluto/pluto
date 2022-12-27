package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttributeTag
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser

internal class ImageViewParser : IParser<ImageView>() {

    override fun getTypeAttributes(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        (view as ImageView).apply {
            val scaleTypeAttribute = Attribute("scale_type", scaleTypeToStr(scaleType), parameterizedTypeString, AttributeTag.ScaleType)
            attributes.add(scaleTypeAttribute)
        }
        return attributes
    }

    companion object {
        private fun scaleTypeToStr(scaleType: ScaleType): String {
            return when (scaleType) {
                ScaleType.CENTER -> "CENTER"
                ScaleType.FIT_XY -> "FIT_XY"
                ScaleType.MATRIX -> "MATRIX"
                ScaleType.FIT_END -> "FIT_END"
                ScaleType.FIT_START -> "FIT_START"
                ScaleType.FIT_CENTER -> "FIT_CENTER"
                ScaleType.CENTER_CROP -> "CENTER_CROP"
                ScaleType.CENTER_INSIDE -> "CENTER_INSIDE"
                else -> "OTHER"
            }
        }
    }


}