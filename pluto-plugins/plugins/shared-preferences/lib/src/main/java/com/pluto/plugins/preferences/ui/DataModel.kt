package com.pluto.plugins.preferences.ui

import androidx.annotation.Keep
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.views.keyvalue.KeyValuePairEditMetaData
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
internal data class SharedPrefFile(
    val label: String,
    val isDefault: Boolean
) : ListItem() {
    override fun isSame(other: Any): Boolean {
        return other is SharedPrefFile && other.label == this.label
    }
}

internal data class SharedPrefKeyValuePair(
    val key: String,
    val value: Any?,
    val prefLabel: String?,
    val isDefault: Boolean = false
) : ListItem(), KeyValuePairEditMetaData
