package com.pluto.plugins.layoutinspector.internal.attributes.data

import com.pluto.utilities.list.ListItem
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal data class Attribute<T>(val title: String, val value: T?, val tag: AttributeTag = AttributeTag.Immutable()) : ListItem() {
    val displayText: CharSequence? = tag.toDisplayText(value)
    val editorRequest: KeyValuePairEditRequest? = tag.toEditorRequestData(title, value)
    fun onUpdate(updatedValue: String) {
        tag.onValueUpdated(title, updatedValue)
    }
}
