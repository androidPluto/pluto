package com.pluto.plugins.exceptions.internal.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pluto.plugin.utilities.extensions.showMoreOptions
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepFragmentThreadStackTraceBinding
import com.pluto.plugins.exceptions.internal.ProcessThread
import com.pluto.plugins.exceptions.internal.ThreadStates
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity
import com.pluto.plugins.exceptions.internal.ui.holder.StackTraceListItemHolder

class ThreadStackTraceFragment : Fragment(R.layout.pluto_excep___fragment_thread_stack_trace) {
    private val binding by viewBinding(PlutoExcepFragmentThreadStackTraceBinding::bind)
    private val viewModel: CrashesViewModel by activityViewModels()
    private val threadAdapter: BaseAdapter by lazy { StackTracesAdapter(onActionListener) }
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    private val contentSharer by lazyContentSharer()
    private var filterValue: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
        binding.list.apply {
            adapter = threadAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.close.setOnDebounceClickListener {
            activity?.onBackPressed()
        }

        binding.filterCta.setOnDebounceClickListener {
            context?.showMoreOptions(it, R.menu.pluto_excep___menu_stack_trace_filter) { item ->
                filterValue = when (item.itemId) {
                    R.id.filterBlocked -> getString(R.string.pluto_excep___trace_filter_blocked)
                    R.id.filterWaiting -> getString(R.string.pluto_excep___trace_filter_waiting)
                    R.id.filterTimedWaiting -> getString(R.string.pluto_excep___trace_filter_timed_waiting)
                    R.id.filterRunnable -> getString(R.string.pluto_excep___trace_filter_runnable)
                    else -> null
                }
                threadAdapter.list = (viewModel.currentException.value?.data?.threadStateList?.states ?: emptyList()).process(filterValue)
                binding.filterCta.text = filterValue ?: getString(R.string.pluto_excep___trace_filter_all)
            }
        }
        binding.share.setOnDebounceClickListener {
            viewModel.currentException.value?.data?.threadStateList?.let {
                contentSharer.share(Shareable(title = "Share Thread Stack Trace", content = it.toShareText(), fileName = "ANR Thread Stack Trace from Pluto"))
            }
        }

        viewModel.currentException.removeObservers(viewLifecycleOwner)
        viewModel.currentException.observe(viewLifecycleOwner, exceptionObserver)
    }

    private val exceptionObserver = Observer<ExceptionEntity> {
        threadAdapter.list = (it.data.threadStateList?.states ?: emptyList()).process(filterValue)
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
        }
    }
}

private fun ThreadStates.toShareText(): String {
    val text = StringBuilder()
    text.append("ANR Thread Stack Trace - ${states.size} threads : \n")
    states.process().forEach {
        text.append("\n* ${it.name}\t ${it.state.uppercase()}\n")
        it.stackTrace.take(StackTraceListItemHolder.MAX_STACK_TRACE_LINES).forEach { trace ->
            text.append("\tat $trace\n")
        }
        val extraTrace = it.stackTrace.size - StackTraceListItemHolder.MAX_STACK_TRACE_LINES
        if (extraTrace > 0) {
            text.append("\t +$extraTrace more lines\n")
        }
    }
    return text.toString()
}

private fun List<ProcessThread>.process(filterValue: String? = null): List<ProcessThread> {
    val list = arrayListOf<ProcessThread>()
    val mainThreadName = "main"
    var mainThread: ProcessThread? = null
    forEach {
        if (filterValue == null || it.state.equals(filterValue, true)) {
            if (it.name == mainThreadName) {
                mainThread = it
            } else {
                list.add(it)
            }
        }
    }
    return arrayListOf<ProcessThread>().apply {
        mainThread?.let { add(it) }
        addAll(list)
    }
}
