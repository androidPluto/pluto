package com.pluto.settings.holders

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.pluto.R
import com.pluto.databinding.PlutoItemSettingsGridSizeBinding
import com.pluto.settings.SettingsGridSizeEntity
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.settings.SettingsPreferences

internal class SettingsGridSizeHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_settings_grid_size), listener) {

    private val binding = PlutoItemSettingsGridSizeBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SettingsGridSizeEntity) {
            if (SettingsPreferences.gridSize > SMALLEST_GRID_DIMEN_ALLOWED) {
                binding.decSizeCta.setOnDebounceClickListener {
                    onAction(DEC_SIZE)
                }
                ImageViewCompat.setImageTintList(binding.decSizeCta, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pluto___dark_40)))
            } else {
                binding.decSizeCta.setOnDebounceClickListener(action = null)
                ImageViewCompat.setImageTintList(binding.decSizeCta, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pluto___dark_10)))
            }
            if (SettingsPreferences.gridSize < LARGEST_GRID_DIMEN_ALLOWED) {
                binding.incSizeCta.setOnDebounceClickListener {
                    onAction(INC_SIZE)
                }
                ImageViewCompat.setImageTintList(binding.incSizeCta, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pluto___dark_40)))
            } else {
                binding.incSizeCta.setOnDebounceClickListener(action = null)
                ImageViewCompat.setImageTintList(binding.incSizeCta, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pluto___dark_10)))
            }

            binding.sizeValue.text = "${SettingsPreferences.gridSize} dp"
        }
    }

    companion object {
        const val INC_SIZE = "inc_size"
        const val DEC_SIZE = "dec_size"
        const val SMALLEST_GRID_DIMEN_ALLOWED = 4
        const val LARGEST_GRID_DIMEN_ALLOWED = 20
    }
}
