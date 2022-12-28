package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttributeTag
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatColor
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatGravity
import com.pluto.utilities.extensions.px2dp

internal class TextViewParser : IParser<TextView>() {

    override fun getTypeAttributes(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        (view as TextView).apply {
            val textAttribute = Attribute("text", text, parameterizedTypeString, AttributeTag.Text)
            attributes.add(textAttribute)
            val textColorAttribute = Attribute("text_color", "#" + formatColor(currentTextColor), parameterizedTypeString, AttributeTag.TextColor)
            attributes.add(textColorAttribute)
            val textHintColorAttribute = Attribute("text_hint_color", "#" + formatColor(currentHintTextColor), parameterizedTypeString)
            attributes.add(textHintColorAttribute)
            val textSizeAttribute = Attribute("text_size", "${textSize.px2dp} dp", parameterizedTypeString, AttributeTag.TextSize)
            attributes.add(textSizeAttribute)
            val gravityAttribute = Attribute("gravity", formatGravity(gravity), parameterizedTypeString)
            attributes.add(gravityAttribute)
            val lineCountAttribute = Attribute("line_count", lineCount.toString(), parameterizedTypeString)
            attributes.add(lineCountAttribute)
            val lineHeightAttribute = Attribute("line_height", "${lineHeight.toFloat().px2dp} dp", parameterizedTypeString)
            attributes.add(lineHeightAttribute)
        }
        return attributes
    }
}
