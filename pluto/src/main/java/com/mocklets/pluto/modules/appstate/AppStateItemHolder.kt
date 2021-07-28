package com.mocklets.pluto.modules.appstate

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemAppStateBinding

internal class AppStateItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_app_state), listener) {

    private val binding = PlutoItemAppStateBinding.bind(itemView)

    private val key = binding.key
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is AppStateItem) {
            key.text = "-  ${item.key}"
            value.text = item.value
            itemView.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
