package com.pluto.plugins.preferences.ui.filter

import android.view.ViewGroup
import com.pluto.plugins.preferences.R
import com.pluto.plugins.preferences.SharedPrefRepo
import com.pluto.plugins.preferences.databinding.PlutoPrefItemFilterBinding
import com.pluto.plugins.preferences.ui.SharedPrefFile
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class FilterItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_pref___item_filter), listener) {

    private val binding = PlutoPrefItemFilterBinding.bind(itemView)
    private val title = binding.title
    private val checkbox = binding.checkbox

    override fun onBind(item: ListItem) {
        if (item is SharedPrefFile) {
            title.setSpan {
                if (item.isDefault) {
                    append(italic(light(fontColor(item.label, context.color(com.pluto.plugin.R.color.pluto___text_dark_60)))))
                } else {
                    append(item.label)
                }
            }
            itemView.setOnDebounceClickListener {
                onAction("click")
            }
            checkbox.isSelected = SharedPrefRepo.getSelectedPreferenceFiles(context).contains(item)
        }
    }
}
