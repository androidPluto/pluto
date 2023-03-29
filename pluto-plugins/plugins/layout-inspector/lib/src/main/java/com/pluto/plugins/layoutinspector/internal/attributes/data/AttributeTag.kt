package com.pluto.plugins.layoutinspector.internal.attributes.data

internal sealed class AttributeTag {
    open class Immutable : AttributeTag() {
        object Gravity: Immutable()
    }
    object Visibility : AttributeTag()
    open class LayoutParams : AttributeTag() {
        object Width : LayoutParams()
        object Height : LayoutParams()
    }

    open class Dimension : AttributeTag() {
        object PaddingLeft : Dimension()
        object PaddingRight : Dimension()
        object PaddingTop : Dimension()
        object PaddingBottom : Dimension()
        object MarginLeft : Dimension()
        object MarginRight : Dimension()
        object MarginTop : Dimension()
        object MarginBottom : Dimension()
        object LineHeight : AttributeTag()
    }

    object Alpha : AttributeTag()
    object TextSize : AttributeTag()
    open class Color: AttributeTag() {
        object Text : Color()
    }
    object Text : AttributeTag()
    object ScaleType : AttributeTag()
}
