package com.pluto.plugins.rooms.db.internal.ui

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemTableSelectorBinding
import com.pluto.plugins.rooms.db.internal.TableModel

internal class TableItemHolder(
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
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
