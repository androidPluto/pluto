package com.mocklets.pluto.logger.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mocklets.pluto.logger.LogData
import com.mocklets.pluto.logger.LogsViewModel
import com.mocklets.pluto.logger.R
import com.mocklets.pluto.logger.Session
import com.mocklets.pluto.logger.databinding.PlutoLoggerFragmentListBinding
import com.mocklets.pluto.logger.ui.list.LogsAdapter
import com.mocklets.pluto.utilities.extensions.hideKeyboard
import com.mocklets.pluto.utilities.extensions.linearLayoutManager
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.CustomItemDecorator
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding

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
        binding.close.setDebounceClickListener {
            activity?.finish()
        }
        binding.search.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
                    Session.loggerSearchText = it
                    logsAdapter.list = filteredLogs(it)
                    if (it.isEmpty()) {
                        binding.logList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.options.setDebounceClickListener {
//            requireContext().showMoreOptions(it, R.menu.pluto___popup_menu_logger) { item ->
//                when (item.itemId) {
//                    R.id.clear -> LogsRepo.deleteAll()
//                }
//            }
        }
        binding.search.setText(Session.loggerSearchText)
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
                    it.hideKeyboard(lifecycleScope) {
                        viewModel.updateCurrentLog(data)
                        findNavController().navigate(R.id.openDetails)
                    }
                }
            }
        }
    }
}
