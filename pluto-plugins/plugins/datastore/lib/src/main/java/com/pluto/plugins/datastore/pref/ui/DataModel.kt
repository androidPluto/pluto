package com.pluto.plugins.datastore.pref.ui

import androidx.annotation.Keep
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorOption
import com.pluto.utilities.views.keyvalue.KeyValuePairEditMetaData

@Keep
internal data class DatastorePrefFile(
    val label: CharSequence,
    val isDefault: Boolean
) : SelectorOption() {
    override fun displayText(): CharSequence = label
}

internal data class DatastorePrefKeyValuePair(
    val key: String,
    val value: Any?,
    val prefLabel: String?,
    val isDefault: Boolean = false
) : ListItem(), KeyValuePairEditMetaData
