package com.pluto.settings

import android.view.ViewGroup
import com.pluto.settings.holders.SettingsEasyAccessHolder
import com.pluto.settings.holders.SettingsEasyAccessPopupAppearanceHolder
import com.pluto.settings.holders.SettingsGridSizeHolder
import com.pluto.settings.holders.SettingsResetAllHolder
import com.pluto.settings.holders.SettingsThemeHolder
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class SettingsAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is SettingsEasyAccessEntity -> ITEM_TYPE_EASY_ACCESS
            is SettingsEasyAccessPopupAppearanceEntity -> ITEM_TYPE_EASY_ACCESS_APPEARANCE
            is SettingsThemeEntity -> ITEM_TYPE_THEME
            is SettingsGridSizeEntity -> ITEM_TYPE_GRID_SIZE
            is SettingsResetAllEntity -> ITEM_TYPE_RESET_ALL
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_EASY_ACCESS -> SettingsEasyAccessHolder(parent, listener)
            ITEM_TYPE_EASY_ACCESS_APPEARANCE -> SettingsEasyAccessPopupAppearanceHolder(parent, listener)
            ITEM_TYPE_THEME -> SettingsThemeHolder(parent, listener)
            ITEM_TYPE_GRID_SIZE -> SettingsGridSizeHolder(parent, listener)
            ITEM_TYPE_RESET_ALL -> SettingsResetAllHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_EASY_ACCESS = 1000
        const val ITEM_TYPE_EASY_ACCESS_APPEARANCE = 1001
        const val ITEM_TYPE_THEME = 1002
        const val ITEM_TYPE_GRID_SIZE = 1003
        const val ITEM_TYPE_RESET_ALL = 1004
    }
}
