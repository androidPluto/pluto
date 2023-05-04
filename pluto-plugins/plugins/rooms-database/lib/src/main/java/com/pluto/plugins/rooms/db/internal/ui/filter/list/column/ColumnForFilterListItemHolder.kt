package com.pluto.plugins.rooms.db.internal.ui.filter.list.column

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemColumnForFilterBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class ColumnForFilterListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener,
    appliedFilters: List<FilterModel>
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_column_for_filter), actionListener) {

    private val binding = PlutoRoomsItemColumnForFilterBinding.bind(itemView)
    private val alreadyUsedColumns: List<ColumnModel> = appliedFilters.map { it.column }

    override fun onBind(item: ListItem) {
        if (item is ColumnModel) {
            if (alreadyUsedColumns.contains(item)) {
                binding.value.setSpan {
                    append(semiBold(fontColor(item.name, context.color(R.color.pluto___text_dark_40))))
                    append(italic(light(fontColor("\t(already applied)", context.color(R.color.pluto___text_dark_40)))))
                }
                binding.root.setOnDebounceClickListener {}
            } else {
                binding.value.setSpan {
                    append(semiBold(fontColor(item.name, context.color(R.color.pluto___text_dark_80))))
                }
                binding.root.setOnDebounceClickListener {
                    onAction("click")
                }
            }
        }
    }
}
