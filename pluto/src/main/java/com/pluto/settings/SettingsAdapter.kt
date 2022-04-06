package com.pluto.settings

import android.view.ViewGroup
import com.pluto.plugins.utilities.list.BaseAdapter
import com.pluto.plugins.utilities.list.DiffAwareHolder
import com.pluto.plugins.utilities.list.ListItem
import com.pluto.settings.holders.SettingsEasyAccessHolder
import com.pluto.settings.holders.SettingsEasyAccessPopupAppearanceHolder
import com.pluto.settings.holders.SettingsResetAllHolder

internal class SettingsAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is SettingsEasyAccessEntity -> ITEM_TYPE_EASY_ACCESS
            is SettingsEasyAccessPopupAppearanceEntity -> ITEM_TYPE_EASY_ACCESS_APPEARANCE
            is SettingsResetAllEntity -> ITEM_TYPE_RESET_ALL
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_EASY_ACCESS -> SettingsEasyAccessHolder(parent, listener)
            ITEM_TYPE_EASY_ACCESS_APPEARANCE -> SettingsEasyAccessPopupAppearanceHolder(parent, listener)
            ITEM_TYPE_RESET_ALL -> SettingsResetAllHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_EASY_ACCESS = 1000
        const val ITEM_TYPE_EASY_ACCESS_APPEARANCE = 1001
        const val ITEM_TYPE_RESET_ALL = 1002
    }
}
