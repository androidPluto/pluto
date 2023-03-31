package com.pluto.plugins.layoutinspector.internal.attributes.data

import com.pluto.utilities.views.keyvalue.KeyValuePairEditMetaData

internal sealed class AttributeTag: KeyValuePairEditMetaData {
    open class Immutable : AttributeTag() {
        object Gravity: Immutable()
    }
    object Visibility : AttributeTag()
    open class LayoutParams : AttributeTag() {
        object Width : LayoutParams()
        object Height : LayoutParams()
    }

    open class Dimension : AttributeTag() {
        object PaddingStart : Dimension()
        object PaddingEnd : Dimension()
        object PaddingTop : Dimension()
        object PaddingBottom : Dimension()
        object MarginStart : Dimension()
        object MarginEnd : Dimension()
        object MarginTop : Dimension()
        object MarginBottom : Dimension()
        object LineHeight : AttributeTag()
    }

    object Alpha : AttributeTag()
    object TextSize : AttributeTag()
    open class Color: AttributeTag() {
        object Text : Color()
        object Hint : Color()
    }
    object Text : AttributeTag()
    object Hint : AttributeTag()
    object ScaleType : AttributeTag()
}
