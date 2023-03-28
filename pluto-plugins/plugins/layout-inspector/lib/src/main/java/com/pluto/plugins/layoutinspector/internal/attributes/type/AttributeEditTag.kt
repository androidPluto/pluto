package com.pluto.plugins.layoutinspector.internal.attributes.type

internal sealed class AttributeEditTag(val hint: String? = null) {
    object Immutable : AttributeEditTag()
    object LayoutWidth : AttributeEditTag("enter value (is dp)")
    object LayoutHeight : AttributeEditTag("enter value (is dp)")
    object Visibility : AttributeEditTag()
    object PaddingLeft : AttributeEditTag("enter value (is dp)")
    object PaddingRight : AttributeEditTag("enter value (is dp)")
    object PaddingTop : AttributeEditTag("enter value (is dp)")
    object PaddingBottom : AttributeEditTag("enter value (is dp)")
    object MarginLeft : AttributeEditTag("enter value (is dp)")
    object MarginRight : AttributeEditTag("enter value (is dp)")
    object MarginTop : AttributeEditTag("enter value (is dp)")
    object MarginBottom : AttributeEditTag("enter value (is dp)")
    object Alpha : AttributeEditTag("choose value (0 to 1)")
    object LineHeight : AttributeEditTag("enter value (is dp)")
    object TextSize : AttributeEditTag("enter value (is sp)")
    object TextColor : AttributeEditTag("enter color hex code (ARGB)")
    object Text : AttributeEditTag("enter value")
    object ScaleType : AttributeEditTag()
}
