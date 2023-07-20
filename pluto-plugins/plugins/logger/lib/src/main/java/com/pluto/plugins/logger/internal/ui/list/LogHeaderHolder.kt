package com.pluto.plugins.logger.internal.ui.list

import android.view.ViewGroup
import com.pluto.plugins.logger.R
import com.pluto.plugins.logger.databinding.PlutoLoggerListHeaderBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class LogHeaderHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_logger___list_header), actionListener) {

    private val binding = PlutoLoggerListHeaderBinding.bind(itemView)
    private val logTag = binding.title

    override fun onBind(item: ListItem) {
        logTag.text = context.getString(R.string.pluto_logger___previous_log_header)
    }
}
