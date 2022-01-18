package com.mocklets.pluto.modules.settings.holders

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.canDrawOverlays
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemSettingsEasyAccessBinding
import com.mocklets.pluto.modules.settings.SettingsEasyAccessEntity

internal class SettingsEasyAccessHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_easy_access), listener) {

    private val binding = PlutoItemSettingsEasyAccessBinding.bind(itemView)
    private val checkbox = binding.checkbox

    override fun onBind(item: ListItem) {
        if (item is SettingsEasyAccessEntity) {
            checkbox.isSelected = itemView.context.canDrawOverlays()
            itemView.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
