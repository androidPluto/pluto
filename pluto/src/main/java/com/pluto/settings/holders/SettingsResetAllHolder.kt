package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsResetAllBinding
import com.pluto.plugins.utilities.extensions.inflate
import com.pluto.plugins.utilities.list.DiffAwareAdapter
import com.pluto.plugins.utilities.list.DiffAwareHolder
import com.pluto.plugins.utilities.list.ListItem
import com.pluto.plugins.utilities.setDebounceClickListener
import com.pluto.settings.SettingsResetAllEntity

internal class SettingsResetAllHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_reset_all), listener) {

    private val binding = PlutoItemSettingsResetAllBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SettingsResetAllEntity) {
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
