package com.pluto.settings.holders

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsEasyAccessBinding
import com.pluto.settings.SettingsEasyAccessEntity
import com.pluto.settings.canDrawOverlays
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class SettingsEasyAccessHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_easy_access), listener) {

    private val binding = PlutoItemSettingsEasyAccessBinding.bind(itemView)
    private val checkbox = binding.checkbox

    override fun onBind(item: ListItem) {
        if (item is SettingsEasyAccessEntity) {
            checkbox.isSelected = itemView.context.canDrawOverlays()
            itemView.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
