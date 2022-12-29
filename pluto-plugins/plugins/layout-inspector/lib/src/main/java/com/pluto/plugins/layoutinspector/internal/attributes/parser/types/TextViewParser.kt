package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeType
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeColor
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeDimenDP
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeDimenSP
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeGravity
import com.pluto.plugins.layoutinspector.internal.attributes.type.MutableAttributeTag

internal class TextViewParser : IParser<TextView>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as TextView).apply {
            attributes.add(Attribute(AttributeType("text", MutableAttributeTag.Text), text))
            attributes.add(Attribute(AttributeTypeColor("text_color", MutableAttributeTag.TextColor), currentTextColor))
            attributes.add(Attribute(AttributeTypeColor("text_hint_color", MutableAttributeTag.TextColor), currentHintTextColor))
            attributes.add(Attribute(AttributeTypeDimenSP("text_size", MutableAttributeTag.TextSize), textSize))
            attributes.add(Attribute(AttributeTypeGravity("gravity"), gravity))
            attributes.add(Attribute(AttributeType("line_count"), lineCount))
            attributes.add(Attribute(AttributeTypeDimenDP("line_height", MutableAttributeTag.LineHeight), lineHeight.toFloat()))
        }
        return attributes
    }
}
