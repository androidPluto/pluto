package com.mocklets.pluto.modules.settings.holders

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemSettingsEasyAccessBinding
import com.mocklets.pluto.modules.settings.SettingsSharedPrefEntity

internal class SettingsSharePreferenceHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_shared_pref), listener) {

    private val binding = PlutoItemSettingsEasyAccessBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SettingsSharedPrefEntity) {
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
