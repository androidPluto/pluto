package com.pluto.plugins.rooms.db.internal.ui.filter.list.filter

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemFilterCreateBinding
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class FilterListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_filter_create), actionListener) {

    private val binding = PlutoRoomsItemFilterCreateBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is FilterModel) {
            binding.column.text = item.column.name
            binding.relation.text = item.relation.symbol
            binding.values.text = item.value
            binding.root.setOnDebounceClickListener {
                onAction(ACTION_EDIT)
            }
            binding.delete.setOnDebounceClickListener {
                onAction(ACTION_DELETE)
            }
        }
    }

    companion object {
        const val ACTION_DELETE = "delete"
        const val ACTION_EDIT = "edit"
    }
}
