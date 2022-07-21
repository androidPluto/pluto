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
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepFragmentThreadStackTraceBinding
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity

class ThreadStackTraceFragment : Fragment(R.layout.pluto_excep___fragment_thread_stack_trace) {
    private val binding by viewBinding(PlutoExcepFragmentThreadStackTraceBinding::bind)
    private val viewModel: CrashesViewModel by activityViewModels()
    private val threadAdapter: BaseAdapter by lazy { CrashesAdapter(onActionListener) }
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//    private val contentSharer by lazyContentSharer()

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
        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }

        viewModel.currentException.removeObservers(viewLifecycleOwner)
        viewModel.currentException.observe(viewLifecycleOwner, exceptionObserver)
    }

    private val exceptionObserver = Observer<ExceptionEntity> {
        threadAdapter.list = it.data.threadStateList?.states ?: emptyList()
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            when (action) {
                "click" -> {
                }
            }
        }
    }
}
