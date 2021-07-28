package com.mocklets.pluto.modules.settings.holders

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemSettingsLinkMockletsBinding
import com.mocklets.pluto.modules.settings.SettingsSharedPrefEntity

internal class SettingsMockletsLinkHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_link_mocklets), listener) {

    private val binding = PlutoItemSettingsLinkMockletsBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SettingsSharedPrefEntity) {
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
