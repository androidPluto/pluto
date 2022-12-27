package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttrEditMode
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.utilities.extensions.px2dp

internal class TextViewParser : IParser<TextView> {
    override fun getAttrs(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        (view as TextView).apply {
            val textAttribute = Attribute("text", text.toString(), AttrEditMode.TEXT)
            attributes.add(textAttribute)
            val textColorAttribute = Attribute("textColor", "#" + intToHex(currentTextColor), AttrEditMode.TEXT_COLOR)
            attributes.add(textColorAttribute)
            val textHintColorAttribute = Attribute("textHintColor", "#" + intToHex(currentHintTextColor))
            attributes.add(textHintColorAttribute)
            val textSizeAttribute = Attribute("textSize", "${textSize.px2dp} dp", AttrEditMode.TEXT_SIZE)
            attributes.add(textSizeAttribute)
            val gravityAttribute = Attribute("gravity", gravityToStr(gravity))
            attributes.add(gravityAttribute)
            val lineCountAttribute = Attribute("lineCount", lineCount.toString())
            attributes.add(lineCountAttribute)
            val lineHeightAttribute = Attribute("lineHeight", "${lineHeight.toFloat().px2dp} dp")
            attributes.add(lineHeightAttribute)
        }
        return attributes
    }

    companion object {
        private fun intToHex(value: Int): String {
            return Integer.toHexString(value).uppercase()
        }

        private fun gravityToStr(gravity: Int): String {
            return when (gravity) {
                Gravity.NO_GRAVITY -> "NO_GRAVITY"
                Gravity.LEFT -> "LEFT"
                Gravity.TOP -> "TOP"
                Gravity.RIGHT -> "RIGHT"
                Gravity.BOTTOM -> "BOTTOM"
                Gravity.CENTER -> "CENTER"
                Gravity.CENTER_HORIZONTAL -> "CENTER_HORIZONTAL"
                Gravity.CENTER_VERTICAL -> "CENTER_VERTICAL"
                Gravity.START -> "START"
                Gravity.END -> "END"
                Gravity.CLIP_HORIZONTAL -> "CLIP_HORIZONTAL"
                Gravity.CLIP_VERTICAL -> "CLIP_VERTICAL"
                Gravity.FILL -> "FILL"
                Gravity.FILL_HORIZONTAL -> "FILL_HORIZONTAL"
                Gravity.FILL_VERTICAL -> "FILL_VERTICAL"
                else -> "OTHER"
            }
        }
    }
}