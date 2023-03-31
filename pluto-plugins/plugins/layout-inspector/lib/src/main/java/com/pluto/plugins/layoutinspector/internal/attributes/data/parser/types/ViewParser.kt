package com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeAlpha
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeDimenDP
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeLayoutParam
import com.pluto.plugins.layoutinspector.internal.attributes.data.mutability.AttributeVisibility
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.IParser

internal class ViewParser : IParser<View>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        val params: ViewGroup.LayoutParams = view.layoutParams
        attributes.add(Attribute("layoutParams", params.javaClass.name))
        attributes.add(AttributeLayoutParam.Width("layout_width", AttributeLayoutParam.Data(params.width, view.width)))
        attributes.add(AttributeLayoutParam.Height("layout_height", AttributeLayoutParam.Data(params.height, view.height)))
        attributes.add(AttributeVisibility("visibility", view.visibility))
        attributes.add(AttributeDimenDP.PaddingStart("padding_start", view.paddingStart.toFloat()))
        attributes.add(AttributeDimenDP.PaddingTop("padding_top", view.paddingTop.toFloat()))
        attributes.add(AttributeDimenDP.PaddingEnd("padding_end", view.paddingEnd.toFloat()))
        attributes.add(AttributeDimenDP.PaddingBottom("padding_bottom", view.paddingBottom.toFloat()))
        if (view.layoutParams != null && view.layoutParams is MarginLayoutParams) {
            val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
            attributes.add(AttributeDimenDP.MarginStart("margin_start", marginLayoutParams.marginStart.toFloat()))
            attributes.add(AttributeDimenDP.MarginTop("margin_top", marginLayoutParams.topMargin.toFloat()))
            attributes.add(AttributeDimenDP.MarginEnd("margin_end", marginLayoutParams.marginEnd.toFloat()))
            attributes.add(AttributeDimenDP.MarginBottom("margin_bottom", marginLayoutParams.bottomMargin.toFloat()))
        }
        attributes.add(Attribute("translationX", view.translationX))
        attributes.add(Attribute("translationY", view.translationY))
        attributes.add(Attribute("background", view.background))
        attributes.add(AttributeAlpha("alpha", view.alpha))
        attributes.add(Attribute("tag", view.tag))
        attributes.add(Attribute("enabled", view.isEnabled))
        attributes.add(Attribute("clickable", view.isClickable))
        attributes.add(Attribute("long_clickable", view.isLongClickable))
        attributes.add(Attribute("focusable", view.isFocusable))
        attributes.add(Attribute("content_dscrptn", view.contentDescription))
        return attributes
    }
}
