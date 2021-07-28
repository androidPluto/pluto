package com.mocklets.pluto.modules.settings.holders

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemSettingsResetAllBinding
import com.mocklets.pluto.modules.settings.SettingsResetAllEntity

internal class SettingsResetAllHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_reset_all), listener) {

    private val binding = PlutoItemSettingsResetAllBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SettingsResetAllEntity) {
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
