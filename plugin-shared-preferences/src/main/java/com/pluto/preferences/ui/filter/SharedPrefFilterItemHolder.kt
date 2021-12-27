package com.pluto.preferences.ui.filter

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.preferences.R
import com.pluto.preferences.SharedPrefRepo
import com.pluto.preferences.databinding.PlutoPrefItemSharedPrefFilterBinding
import com.pluto.preferences.ui.SharedPrefFile

internal class SharedPrefFilterItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_pref___item_shared_pref_filter), listener) {

    private val binding = PlutoPrefItemSharedPrefFilterBinding.bind(itemView)
    private val title = binding.title
    private val checkbox = binding.checkbox

    override fun onBind(item: ListItem) {
        if (item is SharedPrefFile) {
            title.setSpan {
                if (item.isDefault) {
                    append(italic(light(fontColor(item.label, context.color(R.color.pluto___text_dark_60)))))
                } else {
                    append(item.label)
                }
            }
            itemView.setDebounceClickListener {
                onAction("click")
            }
            checkbox.isSelected = SharedPrefRepo.getSelectedPreferenceFiles(context).contains(item)
        }
    }
}
