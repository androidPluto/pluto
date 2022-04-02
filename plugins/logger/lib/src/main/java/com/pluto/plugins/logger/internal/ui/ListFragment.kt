package com.pluto.plugins.logger.internal.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.hideKeyboard
import com.pluto.plugin.utilities.extensions.linearLayoutManager
import com.pluto.plugin.utilities.extensions.showMoreOptions
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.logger.R
import com.pluto.plugins.logger.databinding.PlutoLoggerFragmentListBinding
import com.pluto.plugins.logger.internal.LogData
import com.pluto.plugins.logger.internal.LogsRepo
import com.pluto.plugins.logger.internal.LogsViewModel
import com.pluto.plugins.logger.internal.Session
import com.pluto.plugins.logger.internal.ui.list.LogsAdapter

internal class ListFragment : Fragment(R.layout.pluto_logger___fragment_list) {

    private val binding by viewBinding(PlutoLoggerFragmentListBinding::bind)
    private val viewModel: LogsViewModel by activityViewModels()
    private val logsAdapter: BaseAdapter by lazy { LogsAdapter(onActionListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logList.apply {
            adapter = logsAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.search.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    Session.loggerSearchText = it
                    logsAdapter.list = filteredLogs(it)
                    if (it.isEmpty()) {
                        binding.logList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.search.setText(Session.loggerSearchText)
        viewModel.logs.removeObserver(logsObserver)
        viewModel.logs.observe(viewLifecycleOwner, logsObserver)

        binding.close.setDebounceClickListener {
            activity?.finish()
        }
        binding.options.setDebounceClickListener {
            context?.showMoreOptions(it, R.menu.pluto_logger___menu_more_options) { item ->
                when (item.itemId) {
                    R.id.clear -> LogsRepo.deleteAll()
                }
            }
        }
    }

    private fun filteredLogs(search: String): List<LogData> {
        var list = emptyList<LogData>()
        viewModel.logs.value?.let {
            list = it.filter { log ->
                log.tag.contains(search, true) ||
                    log.message.contains(search, true) ||
                    log.stackTraceElement.fileName.contains(search, true)
            }
        }
        binding.noItemText.text = getString(
            if (search.isNotEmpty()) {
                R.string.pluto_logger___no_search_result
            } else {
                R.string.pluto_logger___no_logs_text
            }
        )
        binding.noItemText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        return list
    }

    private val logsObserver = Observer<List<LogData>> {
        logsAdapter.list = filteredLogs(binding.search.text.toString())
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is LogData) {
                activity?.let {
                    it.hideKeyboard(viewLifecycleOwner.lifecycleScope) {
                        viewModel.updateCurrentLog(data)
                        findNavController().navigate(R.id.openDetails)
                    }
                }
            }
        }
    }
}
