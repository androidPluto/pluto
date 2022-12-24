package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.utilities.extensions.px2dp

class ViewParser : IParser<View> {
    override fun getAttrs(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        val classAttribute = Attribute("class", view.javaClass.name.toString())
        attributes.add(classAttribute)
        val params: ViewGroup.LayoutParams = view.layoutParams
        val paramsAttribute = Attribute("LayoutParams", params.javaClass.name)
        attributes.add(paramsAttribute)
        val widthAttribute = Attribute("layout_width", formatLayoutParam(params.width, view.width), Attribute.Edit.LAYOUT_WIDTH)
        attributes.add(widthAttribute)
        val heightAttribute = Attribute("layout_height", formatLayoutParam(params.height, view.height), Attribute.Edit.LAYOUT_HEIGHT)
        attributes.add(heightAttribute)
        val visibilityAttribute = Attribute("visibility", formatVisibility(view.visibility), Attribute.Edit.VISIBILITY)
        attributes.add(visibilityAttribute)
        val paddingLeftAttribute = Attribute("paddingLeft", "${view.paddingLeft.toFloat().px2dp} dp", Attribute.Edit.PADDING_LEFT)
        attributes.add(paddingLeftAttribute)
        val paddingTopAttribute = Attribute("paddingTop", "${view.paddingTop.toFloat().px2dp} dp", Attribute.Edit.PADDING_TOP)
        attributes.add(paddingTopAttribute)
        val paddingRightAttribute = Attribute("paddingRight", "${view.paddingRight.toFloat().px2dp} dp", Attribute.Edit.PADDING_RIGHT)
        attributes.add(paddingRightAttribute)
        val paddingBottomAttribute = Attribute("paddingBottom", "${view.paddingBottom.toFloat().px2dp} dp", Attribute.Edit.PADDING_BOTTOM)
        attributes.add(paddingBottomAttribute)
        if (view.layoutParams != null && view.layoutParams is MarginLayoutParams) {
            val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
            val marginLeftAttribute = Attribute("marginLeft", "${marginLayoutParams.leftMargin.toFloat().px2dp} dp", Attribute.Edit.MARGIN_LEFT)
            attributes.add(marginLeftAttribute)
            val marginTopAttribute = Attribute("marginTop", "${marginLayoutParams.topMargin.toFloat().px2dp} dp", Attribute.Edit.MARGIN_TOP)
            attributes.add(marginTopAttribute)
            val marginRightAttribute = Attribute("marginRight", "${marginLayoutParams.rightMargin.toFloat().px2dp} dp", Attribute.Edit.MARGIN_RIGHT)
            attributes.add(marginRightAttribute)
            val marginBottomAttribute = Attribute("marginBottom", "${marginLayoutParams.bottomMargin.toFloat().px2dp} dp", Attribute.Edit.MARGIN_BOTTOM)
            attributes.add(marginBottomAttribute)
        }
        val translationXAttribute = Attribute("translationX", "${view.translationX.px2dp} dp")
        attributes.add(translationXAttribute)
        val translationYAttribute = Attribute("translationY", "${view.translationY.px2dp} dp")
        attributes.add(translationYAttribute)
        val backgroundAttribute = Attribute("background", formatDrawable(view.background))
        attributes.add(backgroundAttribute)
        val alphaAttribute = Attribute("alpha", view.alpha.toString(), Attribute.Edit.ALPHA)
        attributes.add(alphaAttribute)
        val tagAttribute = Attribute("tag", view.tag?.toString() ?: "")
        attributes.add(tagAttribute)
        val enableAttribute = Attribute("enable", view.isEnabled.toString())
        attributes.add(enableAttribute)
        val clickAttribute = Attribute("clickable", view.isClickable.toString())
        attributes.add(clickAttribute)
        val longClickableAttribute = Attribute("longClickable", view.isLongClickable.toString())
        attributes.add(longClickableAttribute)
        val focusAttribute = Attribute("focusable", view.isFocusable.toString())
        attributes.add(focusAttribute)
        val contentDescriptionAttribute = Attribute("contentDescription", view.contentDescription?.toString() ?: "")
        attributes.add(contentDescriptionAttribute)
        return attributes
    }

    companion object {
        private fun formatLayoutParam(layoutParam: Int, size: Int): String {
            val dp = "${size.toFloat().px2dp} dp"
            if (layoutParam == ViewGroup.LayoutParams.WRAP_CONTENT) {
                return String.format("wrap_content (%s)", dp)
            }
            return if (layoutParam == ViewGroup.LayoutParams.MATCH_PARENT) {
                String.format("match_parent (%s)", dp)
            } else dp
        }

        private fun formatVisibility(value: Int): String {
            if (value == View.VISIBLE) {
                return "VISIBLE"
            }
            if (value == View.INVISIBLE) {
                return "INVISIBLE"
            }
            return if (value == View.GONE) {
                "GONE"
            } else "OTHER"
        }

        private fun formatDrawable(drawable: Drawable?): String {
            if (drawable == null) {
                return "null"
            }
            return if (drawable is ColorDrawable) {
                String.format("#%06X", drawable.color)
            } else drawable.toString()
        }
    }
}