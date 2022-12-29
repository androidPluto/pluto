package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.LayoutParamDimens
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatDrawable
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeType
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeDimenDP
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeLayoutParams
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeTypeVisibility
import com.pluto.plugins.layoutinspector.internal.attributes.type.MutableAttributeTag

internal class ViewParser : IParser<View>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        val params: ViewGroup.LayoutParams = view.layoutParams
        attributes.add(Attribute(AttributeType("layout_params"), params.javaClass.name))
        attributes.add(Attribute(AttributeTypeLayoutParams("layout_width", MutableAttributeTag.LayoutWidth), LayoutParamDimens(params.width, view.width)))
        attributes.add(Attribute(AttributeTypeLayoutParams("layout_height", MutableAttributeTag.LayoutHeight), LayoutParamDimens(params.height, view.height)))
        attributes.add(Attribute(AttributeTypeVisibility("visibility"), view.visibility))
        attributes.add(Attribute(AttributeTypeDimenDP("padding_left", MutableAttributeTag.PaddingLeft), view.paddingLeft.toFloat()))
        attributes.add(Attribute(AttributeTypeDimenDP("padding_top", MutableAttributeTag.PaddingTop), view.paddingTop.toFloat()))
        attributes.add(Attribute(AttributeTypeDimenDP("padding_right", MutableAttributeTag.PaddingRight), view.paddingRight.toFloat()))
        attributes.add(Attribute(AttributeTypeDimenDP("padding_bottom", MutableAttributeTag.PaddingBottom), view.paddingBottom.toFloat()))
        if (view.layoutParams != null && view.layoutParams is MarginLayoutParams) {
            val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
            attributes.add(Attribute(AttributeTypeDimenDP("margin_left", MutableAttributeTag.MarginLeft), marginLayoutParams.leftMargin.toFloat()))
            attributes.add(Attribute(AttributeTypeDimenDP("margin_top", MutableAttributeTag.MarginTop), marginLayoutParams.topMargin.toFloat()))
            attributes.add(Attribute(AttributeTypeDimenDP("margin_right", MutableAttributeTag.MarginRight), marginLayoutParams.rightMargin.toFloat()))
            attributes.add(Attribute(AttributeTypeDimenDP("margin_bottom", MutableAttributeTag.MarginBottom), marginLayoutParams.bottomMargin.toFloat()))
        }
        attributes.add(Attribute(AttributeType("translationX"), view.translationX))
        attributes.add(Attribute(AttributeType("translationY"), view.translationY))
        attributes.add(Attribute(AttributeType("background"), formatDrawable(view.background)))
        attributes.add(Attribute(AttributeType("alpha", MutableAttributeTag.Alpha), view.alpha))
        attributes.add(Attribute(AttributeType("tag"), view.tag?.toString()))
        attributes.add(Attribute(AttributeType("enabled"), view.isEnabled))
        attributes.add(Attribute(AttributeType("clickable"), view.isClickable))
        attributes.add(Attribute(AttributeType("long_clickable"), view.isLongClickable))
        attributes.add(Attribute(AttributeType("focusable"), view.isFocusable))
        attributes.add(Attribute(AttributeType("content_dscrptn"), view.contentDescription))
        return attributes
    }
}
