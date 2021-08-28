package com.mocklets.pluto.modules.logging.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.hideKeyboard
import com.mocklets.pluto.core.extensions.linearLayoutManager
import com.mocklets.pluto.core.extensions.showMoreOptions
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.CustomItemDecorator
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoFragmentLogsBinding
import com.mocklets.pluto.modules.logging.LogData
import com.mocklets.pluto.modules.logging.LogsRepo

internal class LogsFragment : Fragment(R.layout.pluto___fragment_logs) {

    private val binding by viewBinding(PlutoFragmentLogsBinding::bind)
    private val viewModel: LogsViewModel by viewModels()
    private val logsAdapter: BaseAdapter by lazy { LogsAdapter(onActionListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logList.apply {
            adapter = logsAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.search.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
                    Pluto.session.loggerSearchText = it
                    logsAdapter.list = filteredLogs(it)
                    if (it.isEmpty()) {
                        binding.logList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.more.setDebounceClickListener {
            requireContext().showMoreOptions(it, R.menu.pluto___popup_menu_logger) { item ->
                when (item.itemId) {
                    R.id.clear -> LogsRepo.deleteAll()
                }
            }
        }
        binding.search.setText(Pluto.session.loggerSearchText)
        viewModel.logs.removeObserver(logsObserver)
        viewModel.logs.observe(viewLifecycleOwner, logsObserver)
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
                R.string.pluto___no_search_result
            } else {
                R.string.pluto___no_logs_text
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
                    it.hideKeyboard(lifecycleScope) {
                        LogDetailsDialog(it, data).show()
                    }
                }
            }
        }
    }
}
