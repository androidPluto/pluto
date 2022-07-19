package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsThreadStatesBinding
import com.pluto.plugins.exceptions.internal.ThreadStates
import com.pluto.plugins.exceptions.internal.ui.CrashesAdapter

internal class CrashItemDetailsThreadStatesHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_thread_states), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsThreadStatesBinding.bind(itemView)
    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
        }
    }

    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    private var threadAdapter: BaseAdapter = CrashesAdapter(onActionListener)

    init {
        binding.list.apply {
            adapter = threadAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(CustomItemDecorator(context!!, DECORATOR_DIVIDER_PADDING))
        }
    }

    override fun onBind(item: ListItem) {
        if (item is ThreadStates) {
            threadAdapter.list = item.states
            binding.label.setSpan {
                append(context.getString(R.string.pluto_excep___thread_states_label))
                append(fontColor(" (${item.states.size})", context.color(R.color.pluto___text_dark_40)))
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
