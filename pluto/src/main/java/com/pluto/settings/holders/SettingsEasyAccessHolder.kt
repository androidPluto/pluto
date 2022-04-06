package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsEasyAccessBinding
import com.pluto.plugins.utilities.extensions.inflate
import com.pluto.plugins.utilities.list.DiffAwareAdapter
import com.pluto.plugins.utilities.list.DiffAwareHolder
import com.pluto.plugins.utilities.list.ListItem
import com.pluto.plugins.utilities.setDebounceClickListener
import com.pluto.settings.SettingsEasyAccessEntity
import com.pluto.settings.canDrawOverlays

internal class SettingsEasyAccessHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_easy_access), listener) {

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
