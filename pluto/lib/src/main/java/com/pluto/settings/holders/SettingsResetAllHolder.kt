package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsResetAllBinding
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.settings.SettingsResetAllEntity

internal class SettingsResetAllHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_reset_all), listener) {

    private val binding = PlutoItemSettingsResetAllBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SettingsResetAllEntity) {
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
