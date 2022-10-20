package com.pluto.tools

import androidx.annotation.DrawableRes
import com.pluto.utilities.list.ListItem

data class ToolConfiguration(
    val name: String,
    @DrawableRes val icon: Int
) : ListItem() {
    val identifier = name.lowercase().replace(" ", "_", true)

    override fun equals(other: Any?): Boolean {
        return other is ToolConfiguration && identifier == other.identifier
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
