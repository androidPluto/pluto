package com.mocklets.pluto.modules.exceptions.ui.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.dp
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.CustomItemDecorator
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemCrashDetailsThreadStatesBinding
import com.mocklets.pluto.modules.exceptions.ThreadStates
import com.mocklets.pluto.modules.exceptions.ui.CrashesAdapter

internal class CrashItemDetailsThreadStatesHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_crash_details_thread_states), actionListener) {

    private val binding = PlutoItemCrashDetailsThreadStatesBinding.bind(itemView)
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
                append(context.getString(R.string.pluto___thread_states_label))
                append(fontColor(" (${item.states.size})", context.color(R.color.pluto___text_dark_40)))
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
