package com.pluto.plugins.layoutinspector.internal.attributes.type

internal sealed class MutableAttributeTag(val hint: String? = null) {
    object Immutable : MutableAttributeTag()
    object LayoutWidth : MutableAttributeTag("enter value (is dp)")
    object LayoutHeight : MutableAttributeTag("enter value (is dp)")
    object Visibility : MutableAttributeTag()
    object PaddingLeft : MutableAttributeTag("enter value (is dp)")
    object PaddingRight : MutableAttributeTag("enter value (is dp)")
    object PaddingTop : MutableAttributeTag("enter value (is dp)")
    object PaddingBottom : MutableAttributeTag("enter value (is dp)")
    object MarginLeft : MutableAttributeTag("enter value (is dp)")
    object MarginRight : MutableAttributeTag("enter value (is dp)")
    object MarginTop : MutableAttributeTag("enter value (is dp)")
    object MarginBottom : MutableAttributeTag("enter value (is dp)")
    object Alpha : MutableAttributeTag("choose value (0 to 1)")
    object LineHeight : MutableAttributeTag("enter value (is dp)")
    object TextSize : MutableAttributeTag("enter value (is sp)")
    object TextColor : MutableAttributeTag("enter color hex code (ARGB)")
    object Text : MutableAttributeTag("enter value")
    object ScaleType : MutableAttributeTag()
}