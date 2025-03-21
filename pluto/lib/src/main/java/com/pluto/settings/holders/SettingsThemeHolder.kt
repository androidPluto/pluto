package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsThemeBinding
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.settings.SettingsThemeEntity
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

/**
 * ViewHolder for the theme setting item in the settings list.
 *
 * This holder displays a checkbox that indicates whether dark theme is enabled or disabled,
 * and allows the user to toggle between light and dark themes by clicking on the item.
 *
 * @param parent The parent ViewGroup that this holder will be attached to
 * @param listener The action listener that will handle click events on this item
 */
internal class SettingsThemeHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_theme), listener) {

    private val binding = PlutoItemSettingsThemeBinding.bind(itemView)
    private val checkbox = binding.checkbox

    /**
     * Binds the view holder to the provided list item.
     * Updates the checkbox state based on the current theme setting and sets up the click listener.
     *
     * @param item The list item to bind to this holder, expected to be a SettingsThemeEntity
     */
    override fun onBind(item: ListItem) {
        if (item is SettingsThemeEntity) {
            checkbox.isSelected = SettingsPreferences.isDarkThemeEnabled
            itemView.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
