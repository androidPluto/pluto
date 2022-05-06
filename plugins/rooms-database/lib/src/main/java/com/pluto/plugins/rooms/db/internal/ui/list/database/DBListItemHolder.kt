package com.pluto.plugins.rooms.db.internal.ui.list.database

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemDbSelectorBinding
import com.pluto.plugins.rooms.db.internal.DatabaseModel

internal class DBListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_db_selector), actionListener) {

    private val binding = PlutoRoomsItemDbSelectorBinding.bind(itemView)
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is DatabaseModel) {
            value.text = item.name
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
