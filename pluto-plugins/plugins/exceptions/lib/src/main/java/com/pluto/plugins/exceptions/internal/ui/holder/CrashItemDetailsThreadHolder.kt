package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsThreadBinding
import com.pluto.plugins.exceptions.internal.ThreadData
import com.pluto.plugins.exceptions.internal.getStateStringSpan

internal class CrashItemDetailsThreadHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_thread), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsThreadBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ThreadData) {
            setupTabularData(item)
        }
    }

    private fun setupTabularData(item: ThreadData) {
        val dataList = arrayListOf<KeyValuePairData>().apply {
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___thread_name_label),
                    value = context.createSpan {
                        append(semiBold("${item.name.uppercase()}\t"))
                        append(fontColor("(", context.color(com.pluto.plugin.R.color.pluto___text_dark_60)))
                        append(fontColor("id: ", context.color(com.pluto.plugin.R.color.pluto___text_dark_60)))
                        append(bold(fontColor("${item.id}", context.color(com.pluto.plugin.R.color.pluto___text_dark_60))))
                        append(fontColor(")", context.color(com.pluto.plugin.R.color.pluto___text_dark_60)))
                    }
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___priority_label),
                    value = context.createSpan {
                        append(item.priorityString)
                    }
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___daemon_label),
                    value = context.createSpan {
                        append(bold(item.isDaemon.toString()))
                    }
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___thread_run_state_label),
                    value = getStateStringSpan(context, item.state)
                )
            )
        }
        binding.table.set(
            title = context.getString(R.string.pluto_excep___thread_state_label),
            keyValuePairs = dataList
        )
    }
}
