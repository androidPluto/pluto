package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsThemeBinding
import com.pluto.settings.SettingsThemeEntity
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.settings.SettingsPreferences

internal class SettingsThemeHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_theme), listener) {

    private val binding = PlutoItemSettingsThemeBinding.bind(itemView)
    private val checkbox = binding.checkbox

    override fun onBind(item: ListItem) {
        if (item is SettingsThemeEntity) {
            checkbox.isSelected = SettingsPreferences.isDarkThemeEnabled
            itemView.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
