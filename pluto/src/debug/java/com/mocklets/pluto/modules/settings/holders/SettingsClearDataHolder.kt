package com.mocklets.pluto.modules.settings.holders

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemSettingsClearDataBinding
import com.mocklets.pluto.modules.settings.SettingsClearDataEntity

internal class SettingsClearDataHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_clear_data), listener) {

    private val binding = PlutoItemSettingsClearDataBinding.bind(itemView)
    private val title = binding.title
    private val cta = binding.cta

    override fun onBind(item: ListItem) {
        if (item is SettingsClearDataEntity) {
            title.text = item.label
            cta.setDebounceClickListener {
                onAction(item.id)
            }
        }
    }
}
