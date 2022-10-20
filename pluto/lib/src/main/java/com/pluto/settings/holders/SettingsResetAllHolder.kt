package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsResetAllBinding
import com.pluto.settings.SettingsResetAllEntity
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

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
