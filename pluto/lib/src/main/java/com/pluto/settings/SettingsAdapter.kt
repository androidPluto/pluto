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

/**
 * Adapter for the settings list that handles different types of setting items.
 *
 * This adapter is responsible for creating the appropriate view holders for each type of setting item
 * and binding them to the corresponding data. It supports various setting types including easy access,
 * appearance, theme, grid size, and reset all options.
 *
 * @param listener The action listener that will handle interactions with the settings items
 */
internal class SettingsAdapter(private val listener: OnActionListener) : BaseAdapter() {
    /**
     * Determines the view type for a given list item.
     * This is used to create the appropriate view holder for each type of setting.
     *
     * @param item The list item to determine the view type for
     * @return The integer view type code, or null if the item type is not supported
     */
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

    /**
     * Creates the appropriate view holder for the given view type.
     *
     * @param parent The parent view group that will contain the view holder
     * @param viewType The view type code determined by getItemViewType
     * @return The created view holder, or null if the view type is not supported
     */
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
        /** View type constant for the easy access setting item */
        const val ITEM_TYPE_EASY_ACCESS = 1000

        /** View type constant for the easy access popup appearance setting item */
        const val ITEM_TYPE_EASY_ACCESS_APPEARANCE = 1001

        /** View type constant for the theme setting item */
        const val ITEM_TYPE_THEME = 1002

        /** View type constant for the grid size setting item */
        const val ITEM_TYPE_GRID_SIZE = 1003

        /** View type constant for the reset all setting item */
        const val ITEM_TYPE_RESET_ALL = 1004
    }
}
