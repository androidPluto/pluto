package com.pluto.preferences.ui

import androidx.annotation.Keep
import com.mocklets.pluto.plugin.utilities.list.ListItem

@Keep
data class SharedPrefFile(
    val label: String,
    val isDefault: Boolean
) : ListItem() {
    override fun isSame(other: Any): Boolean {
        return other is SharedPrefFile && other.label == this.label
    }
}

data class SharedPrefKeyValuePair(
    val key: String,
    val value: Any?,
    val prefLabel: String?,
    val isDefault: Boolean = false
) : ListItem()
