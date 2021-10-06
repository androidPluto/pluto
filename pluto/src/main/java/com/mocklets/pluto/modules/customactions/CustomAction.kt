package com.mocklets.pluto.modules.customactions

import androidx.annotation.Keep
import com.mocklets.pluto.core.ui.list.ListItem

@Keep
data class CustomAction(
    val title: String,
    val shouldClosePluto: Boolean,
    val clickListener: OnCustomActionListener
) : ListItem() {

    override fun isSame(other: Any): Boolean {
        return other is CustomAction && other.title == title
    }

    override fun isEqual(other: Any): Boolean {
        return other is CustomAction && other.title == title
    }
}

interface OnCustomActionListener {
    fun onClick()
}
