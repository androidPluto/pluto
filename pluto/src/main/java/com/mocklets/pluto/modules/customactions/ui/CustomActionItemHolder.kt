package com.mocklets.pluto.modules.customactions.ui

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.databinding.PlutoItemCustomActionBinding
import com.mocklets.pluto.modules.customactions.CustomAction

internal class CustomActionItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_custom_action), actionListener) {

    private val binding = PlutoItemCustomActionBinding.bind(itemView)
    private val title = binding.title
    private val root = binding.frameRoot

    override fun onBind(item: ListItem) {
        if (item is CustomAction) {
            title.text = item.title
            root.setOnClickListener {
                onAction("click")
            }
        }
    }
}
