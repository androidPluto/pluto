package com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.pluto.plugins.layoutinspector.internal.attributes.data.AttributeTag
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.IParser
import com.pluto.plugins.layoutinspector.internal.attributes.data.LayoutParamDimens

internal class ViewParser : IParser<View>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        val params: ViewGroup.LayoutParams = view.layoutParams
        attributes.add(Attribute("layoutParams", params.javaClass.name))
        attributes.add(Attribute("layout_width", LayoutParamDimens(params.width, view.width), AttributeTag.LayoutParams.Width))
        attributes.add(Attribute("layout_height", LayoutParamDimens(params.height, view.height), AttributeTag.LayoutParams.Height))
        attributes.add(Attribute("visibility", view.visibility, AttributeTag.Visibility))
        attributes.add(Attribute("padding_left", view.paddingLeft.toFloat(), AttributeTag.Dimension.PaddingLeft))
        attributes.add(Attribute("padding_top", view.paddingTop.toFloat(), AttributeTag.Dimension.PaddingTop))
        attributes.add(Attribute("padding_right", view.paddingRight.toFloat(), AttributeTag.Dimension.PaddingRight))
        attributes.add(Attribute("padding_bottom", view.paddingBottom.toFloat(), AttributeTag.Dimension.PaddingBottom))
        if (view.layoutParams != null && view.layoutParams is MarginLayoutParams) {
            val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
            attributes.add(Attribute("margin_left", marginLayoutParams.leftMargin.toFloat(), AttributeTag.Dimension.MarginLeft))
            attributes.add(Attribute("margin_top", marginLayoutParams.topMargin.toFloat(), AttributeTag.Dimension.MarginTop))
            attributes.add(Attribute("margin_right", marginLayoutParams.rightMargin.toFloat(), AttributeTag.Dimension.MarginRight))
            attributes.add(Attribute("margin_bottom", marginLayoutParams.bottomMargin.toFloat(), AttributeTag.Dimension.MarginBottom))
        }
        attributes.add(Attribute("translationX", view.translationX))
        attributes.add(Attribute("translationY", view.translationY))
        attributes.add(Attribute("background", view.background))
        attributes.add(Attribute("alpha", view.alpha, AttributeTag.Alpha))
        attributes.add(Attribute("tag", view.tag))
        attributes.add(Attribute("enabled", view.isEnabled))
        attributes.add(Attribute("clickable", view.isClickable))
        attributes.add(Attribute("long_clickable", view.isLongClickable))
        attributes.add(Attribute("focusable", view.isFocusable))
        attributes.add(Attribute("content_dscrptn", view.contentDescription))
        return attributes
    }
}
