package com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types

import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.data.AttributeTag
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.IParser

internal class TextViewParser : IParser<TextView>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as TextView).apply {
            attributes.add(Attribute("text", text, AttributeTag.Text))
            attributes.add(Attribute("text_color", currentTextColor, AttributeTag.Color.Text))
            attributes.add(Attribute("hint", hint, AttributeTag.Hint))
            attributes.add(Attribute("text_hint_color", currentHintTextColor, AttributeTag.Color.Hint))
            attributes.add(Attribute("text_size", textSize, AttributeTag.TextSize))
            attributes.add(Attribute("gravity", gravity, AttributeTag.Immutable.Gravity))
            attributes.add(Attribute("line_count", lineCount))
            attributes.add(Attribute("line_height", lineHeight.toFloat(), AttributeTag.Dimension.LineHeight))
        }
        return attributes
    }
}
