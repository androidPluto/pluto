package com.mocklets.pluto.modules.settings.holders

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.canDrawOverlays
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.preferences.Preferences
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemSettingsEasyAccessAppearanceBinding
import com.mocklets.pluto.modules.settings.SettingsEasyAccessPopupAppearanceEntity

internal class SettingsEasyAccessPopupAppearanceHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_easy_access_appearance), listener) {

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
                    "mode" -> Preferences(context).isDarkAccessPopup
                    "handed" -> Preferences(context).isRightHandedAccessPopup
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
