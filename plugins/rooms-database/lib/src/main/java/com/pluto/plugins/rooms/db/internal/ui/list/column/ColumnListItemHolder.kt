package com.pluto.plugins.rooms.db.internal.ui.list.column

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemTableSelectorBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

internal class ColumnListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener?
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_column_details), actionListener) {

    private val binding = PlutoRoomsItemTableSelectorBinding.bind(itemView)
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is ColumnModel) {
            value.setSpan { append(item.name) }
        }
    }
}
