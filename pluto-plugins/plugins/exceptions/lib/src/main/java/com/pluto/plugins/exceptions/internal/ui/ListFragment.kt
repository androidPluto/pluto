package com.pluto.plugins.exceptions.internal.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugins.exceptions.PlutoExceptions
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepFragmentListBinding
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity
import com.pluto.utilities.autoCleared
import com.pluto.utilities.extensions.hideKeyboard
import com.pluto.utilities.extensions.linearLayoutManager
import com.pluto.utilities.extensions.showMoreOptions
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

class ListFragment : Fragment(R.layout.pluto_excep___fragment_list) {

    private val binding by viewBinding(PlutoExcepFragmentListBinding::bind)
    private val viewModel: CrashesViewModel by activityViewModels()
    private val crashAdapter by autoCleared<BaseAdapter>{
        CrashesAdapter(onActionListener)
    }
    private var isFetchingInProgress: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.crashList.apply {
            adapter = crashAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.search.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    PlutoExceptions.session.lastSearchText = it
                    crashAdapter.list = filteredLogs(it)
                    if (it.isEmpty()) {
                        binding.crashList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.search.setText(PlutoExceptions.session.lastSearchText)
        binding.close.setOnDebounceClickListener {
            requireActivity().finish()
        }
        binding.options.setOnDebounceClickListener {
            context?.showMoreOptions(it, R.menu.pluto_excep___menu_more_options) { item ->
                when (item.itemId) {
                    R.id.clear -> viewModel.deleteAll()
                }
            }
        }

        isFetchingInProgress = true
        viewModel.fetchAll()

        viewModel.exceptions.removeObserver(exceptionObserver)
        viewModel.exceptions.observe(viewLifecycleOwner, exceptionObserver)
    }

    private val exceptionObserver = Observer<List<ExceptionEntity>> {
        isFetchingInProgress = false
        crashAdapter.list = filteredLogs(binding.search.text.toString())
    }

    private fun filteredLogs(search: String): List<ExceptionEntity> {
        var list = emptyList<ExceptionEntity>()
        if (isFetchingInProgress) {
            binding.loaderGroup.visibility = VISIBLE
        } else {
            binding.loaderGroup.visibility = GONE
            viewModel.exceptions.value?.let {
                list = it.filter { exception ->
                    (exception.data.exception.name ?: "").contains(search, true) ||
                        (exception.data.exception.file ?: "").contains(search, true)
                }
            }
            binding.noItemText.text = getString(
                if (search.isNotEmpty()) R.string.pluto_excep___no_search_result else R.string.pluto_excep___no_crashes_text
            )
            binding.noItemText.visibility = if (list.isEmpty()) VISIBLE else GONE
        }
        return list
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is ExceptionEntity) {
                requireActivity().hideKeyboard()
                if (data.id != null) {
                    viewModel.fetch(data.id)
                    findNavController().navigate(R.id.openDetails)
                } else {
                    requireContext().toast(getString(R.string.pluto_excep___invalid_id))
                }
            }
        }
    }
}
