package com.mocklets.pluto.modules.preferences.ui.filter

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemSharedPrefFilterBinding
import com.mocklets.pluto.modules.preferences.SharedPrefRepo
import com.mocklets.pluto.modules.preferences.ui.SharedPrefFile

internal class SharedPrefFilterItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_shared_pref_filter), listener) {

    private val binding = PlutoItemSharedPrefFilterBinding.bind(itemView)
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
