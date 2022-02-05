package com.pluto.settings.holders

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsEasyAccessAppearanceBinding
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.settings.SettingsEasyAccessPopupAppearanceEntity
import com.pluto.settings.SettingsPreferences
import com.pluto.settings.canDrawOverlays

internal class SettingsEasyAccessPopupAppearanceHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_easy_access_appearance), listener) {

    private val binding = PlutoItemSettingsEasyAccessAppearanceBinding.bind(itemView)
    private val title = binding.title
    private val checkbox = binding.checkbox
    private val disableOverlay = binding.disableOverlay

    override fun onBind(item: ListItem) {
        if (item is SettingsEasyAccessPopupAppearanceEntity) {
            disableOverlay.visibility = if (itemView.context.canDrawOverlays()) GONE else VISIBLE
            title.text = context.getString(
                when (item.type) {
                    "mode" -> R.string.pluto___settings_easy_access_appearance_mode_title
                    "handed" -> R.string.pluto___settings_easy_access_appearance_handed_title
                    else -> error("unsupported appearance type")
                }
            )
            checkbox.isSelected =
                when (item.type) {
                    "mode" -> SettingsPreferences.isDarkAccessPopup
                    "handed" -> SettingsPreferences.isRightHandedAccessPopup
                    else -> error("unsupported appearance type")
                }

            if (itemView.context.canDrawOverlays()) {
                itemView.setDebounceClickListener {
                    onAction("click")
                }
            }
        }
    }
}
