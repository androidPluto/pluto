package com.pluto.plugins.rooms.db.internal.ui.filter.list.relation

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemFilterRelationBinding
import com.pluto.plugins.rooms.db.internal.FilterRelation
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class FilterRelationListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_filter_relation), actionListener) {

    private val binding = PlutoRoomsItemFilterRelationBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is FilterRelation) {
            binding.name.setSpan {
                append(bold(fontColor(item.symbol, context.color(R.color.pluto___text_dark_80))))
                append(italic("\t\t(${item.javaClass.simpleName})"))
            }
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
