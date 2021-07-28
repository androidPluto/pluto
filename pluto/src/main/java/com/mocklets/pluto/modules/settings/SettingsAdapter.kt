package com.mocklets.pluto.modules.settings

import android.view.ViewGroup
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.settings.holders.SettingsClearDataHolder
import com.mocklets.pluto.modules.settings.holders.SettingsEasyAccessHolder
import com.mocklets.pluto.modules.settings.holders.SettingsEasyAccessPopupAppearanceHolder
import com.mocklets.pluto.modules.settings.holders.SettingsMockletsLinkHolder
import com.mocklets.pluto.modules.settings.holders.SettingsResetAllHolder
import com.mocklets.pluto.modules.settings.holders.SettingsSharePreferenceHolder

internal class SettingsAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is SettingsEasyAccessEntity -> ITEM_TYPE_EASY_ACCESS
            is SettingsClearDataEntity -> ITEM_TYPE_CLEAR_CRASHES
            is SettingsSharedPrefEntity -> ITEM_TYPE_SHARED_PREF
            is SettingsLinkMockletsEntity -> ITEM_TYPE_MOCKLETS
            is SettingsEasyAccessPopupAppearanceEntity -> ITEM_TYPE_EASY_ACCESS_APPEARANCE
            is SettingsResetAllEntity -> ITEM_TYPE_RESET_ALL
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_EASY_ACCESS -> SettingsEasyAccessHolder(parent, listener)
            ITEM_TYPE_CLEAR_CRASHES -> SettingsClearDataHolder(parent, listener)
            ITEM_TYPE_SHARED_PREF -> SettingsSharePreferenceHolder(parent, listener)
            ITEM_TYPE_MOCKLETS -> SettingsMockletsLinkHolder(parent, listener)
            ITEM_TYPE_EASY_ACCESS_APPEARANCE -> SettingsEasyAccessPopupAppearanceHolder(parent, listener)
            ITEM_TYPE_RESET_ALL -> SettingsResetAllHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_EASY_ACCESS = 1000
        const val ITEM_TYPE_CLEAR_CRASHES = 1001
        const val ITEM_TYPE_SHARED_PREF = 1002
        const val ITEM_TYPE_MOCKLETS = 1003
        const val ITEM_TYPE_EASY_ACCESS_APPEARANCE = 1004
        const val ITEM_TYPE_RESET_ALL = 1005
    }
}
