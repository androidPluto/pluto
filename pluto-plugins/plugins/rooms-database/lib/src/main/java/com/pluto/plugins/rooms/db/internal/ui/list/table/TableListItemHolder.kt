package com.pluto.plugins.rooms.db.internal.ui.list.table

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemTableSelectorBinding
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class TableListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_table_selector), actionListener) {

    private val binding = PlutoRoomsItemTableSelectorBinding.bind(itemView)
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is TableModel) {
            value.setSpan {
                append(
                    fontColor(
                        item.name,
                        context.color(
                            if (item.isSystemTable) {
                                R.color.pluto___text_dark_40
                            } else {
                                R.color.pluto___text_dark_80
                            }
                        )
                    )
                )
            }
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
