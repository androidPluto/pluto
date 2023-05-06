package com.pluto.plugins.rooms.db.internal.ui.filter.list.filter

import android.content.Context
import android.view.ViewGroup
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsItemFilterCreateBinding
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.FilterRelation
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.createSpan
import com.pluto.utilities.spannable.setSpan

internal class FilterListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_rooms___item_filter_create), actionListener) {

    private val binding = PlutoRoomsItemFilterCreateBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is FilterModel) {
            binding.column.text = item.column.name
            binding.relation.text = item.relation.symbol
            binding.values.setSpan { append(getFormattedValue(context, item)) }
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

    private fun getFormattedValue(context: Context, item: FilterModel): CharSequence {
        return when (item.relation) {
            is FilterRelation.Between -> {
                val split = item.value?.split(",")
                if (!split.isNullOrEmpty()) {
                    context.createSpan {
                        append(regular(split[0].trim()))
                        append(light(fontColor(" & ", context.color(R.color.pluto___text_dark_40))))
                        append(regular(split[1].trim()))
                    }
                } else {
                    item.value ?: ""
                }
            }

            is FilterRelation.In -> {
                val split = item.value?.split(",")
                if (!split.isNullOrEmpty()) {
                    context.createSpan {
                        split.forEachIndexed { index, value ->
                            if (value == "") {
                                append(light(italic(fontColor("blank", context.color(R.color.pluto___text_dark_40)))))
                            } else {
                                append(regular(value.trim()))
                            }
                            if (index < split.lastIndex) {
                                append(light(fontColor(" , ", context.color(R.color.pluto___text_dark_40))))
                            }
                        }
                    }
                } else {
                    item.value ?: ""
                }
            }

            else -> context.createSpan {
                when (item.value) {
                    "" -> append(light(italic(fontColor("blank", context.color(R.color.pluto___text_dark_40)))))
                    null -> append(light(italic(fontColor("null", context.color(R.color.pluto___text_dark_40)))))
                    else -> append(item.value)
                }
            }
        }
    }
}
