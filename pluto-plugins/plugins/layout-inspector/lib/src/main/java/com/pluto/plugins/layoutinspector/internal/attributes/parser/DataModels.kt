package com.pluto.plugins.layoutinspector.internal.attributes.parser

import com.pluto.utilities.list.ListItem

internal class Attribute(val key: String, val value: CharSequence?, val parameterizedType: String, val tag: AttributeTag? = null) : ListItem()

internal sealed class AttributeTag {
    object LayoutWidth: AttributeTag()
    object LayoutHeight: AttributeTag()
    object Visibility: AttributeTag()
    object PaddingLeft: AttributeTag()
    object PaddingRight: AttributeTag()
    object PaddingTop: AttributeTag()
    object PaddingBottom: AttributeTag()
    object MarginLeft: AttributeTag()
    object MarginRight: AttributeTag()
    object MarginTop: AttributeTag()
    object MarginBottom: AttributeTag()
    object Alpha: AttributeTag()
    object TextSize: AttributeTag()
    object TextColor: AttributeTag()
    object Text: AttributeTag()
    object ScaleType: AttributeTag()

}
