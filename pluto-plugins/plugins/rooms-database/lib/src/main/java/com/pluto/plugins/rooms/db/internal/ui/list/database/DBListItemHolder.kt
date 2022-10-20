package com.pluto.plugins.rooms.db.internal.ui.list.database

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemDbSelectorBinding
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class DBListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_db_selector), actionListener) {

    private val binding = PlutoRoomsItemDbSelectorBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is DatabaseModel) {
            binding.value.text = item.name
            binding.dbClassName.text = "${item.dbClass.simpleName}.kt"
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
