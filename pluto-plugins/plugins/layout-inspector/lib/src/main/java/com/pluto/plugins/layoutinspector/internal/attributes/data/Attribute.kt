package com.pluto.plugins.layoutinspector.internal.attributes.data

import com.pluto.utilities.list.ListItem
import com.pluto.utilities.views.keyvalue.KeyValuePairEditMetaData

internal open class Attribute<T>(val title: String, val value: T) :
    ListItem(),
    KeyValuePairEditMetaData {
    open fun displayText(): CharSequence? = if (value is CharSequence) value else value?.toString()

    override fun isSame(other: Any): Boolean {
        return other is Attribute<*> && other.title == title
    }
}
