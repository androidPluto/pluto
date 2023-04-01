package com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types

import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeColor
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeDimenSP
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeGravity
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeText
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.IParser

internal class TextViewParser : IParser<TextView>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as TextView).apply {
            attributes.add(AttributeText.Text("text", text))
            attributes.add(AttributeColor.Text("text_color", currentTextColor))
            attributes.add(AttributeText.Hint("hint", hint))
            attributes.add(AttributeColor.Hint("text_hint_color", currentHintTextColor))
            attributes.add(AttributeDimenSP.TextSize("text_size", textSize))
            attributes.add(AttributeGravity("gravity", gravity))
            attributes.add(Attribute("line_count", lineCount))
            attributes.add(Attribute("line_height", lineHeight.toFloat()))
        }
        return attributes
    }
}
