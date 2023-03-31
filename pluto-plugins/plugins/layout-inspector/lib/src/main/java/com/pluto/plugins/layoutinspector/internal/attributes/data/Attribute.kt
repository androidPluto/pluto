package com.pluto.plugins.layoutinspector.internal.attributes.data

import com.pluto.utilities.list.ListItem
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal data class Attribute<T>(val title: String, val value: T?, val tag: AttributeTag = AttributeTag.Immutable()) : ListItem() {
    val displayText: CharSequence? = tag.toDisplayText(value)
    val editorRequestData: KeyValuePairEditRequest? = toEditorRequestData(title, value)
}
