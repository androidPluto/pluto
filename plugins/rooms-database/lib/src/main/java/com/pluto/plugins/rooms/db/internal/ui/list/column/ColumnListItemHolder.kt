package com.pluto.plugins.rooms.db.internal.ui.list.column

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemColumnDetailsBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

internal class ColumnListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener?
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_column_details), actionListener) {

    private val binding = PlutoRoomsItemColumnDetailsBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ColumnModel) {
            binding.title.setSpan { append("- ${item.name}") }
            binding.primaryKey.visibility = if (item.isPrimaryKey) VISIBLE else GONE
            binding.type.setSpan {
                append("Data type: ")
                append(bold("${item.type}, "))
                if (item.isNotNull) {
                    append(bold("NOT_NULL"))
                } else {
                    append(bold("NULL"))
                }
            }
            item.defaultValue?.let {
                binding.defaultValue.visibility = VISIBLE
                binding.defaultValue.setSpan {
                    append("Default value: ")
                    append(bold(it))
                }
            } ?: run {
                binding.defaultValue.visibility = GONE
            }
        }
    }
}
