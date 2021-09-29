package com.mocklets.pluto.modules.exceptions.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.hideKeyboard
import com.mocklets.pluto.core.extensions.linearLayoutManager
import com.mocklets.pluto.core.extensions.showMoreOptions
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.CustomItemDecorator
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.routing.Screens
import com.mocklets.pluto.core.ui.routing.lazyRouter
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoFragmentCrashesBinding
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity

internal class CrashesFragment : Fragment(R.layout.pluto___fragment_crashes) {

    private val binding by viewBinding(PlutoFragmentCrashesBinding::bind)
    private val viewModel: CrashesViewModel by activityViewModels()
    private val crashAdapter: BaseAdapter by lazy { CrashesAdapter(onActionListener) }
    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logList.apply {
            adapter = crashAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.search.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
                    Pluto.session.exceptionSearchText = it
                    crashAdapter.list = filteredLogs(it)
                    if (it.isEmpty()) {
                        binding.logList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.more.setDebounceClickListener {
            requireContext().showMoreOptions(it, R.menu.pluto___popup_menu_crashes) { item ->
                when (item.itemId) {
                    R.id.clear -> viewModel.deleteAll()
                }
            }
        }
        binding.search.setText(Pluto.session.exceptionSearchText)
        viewModel.exceptions.removeObserver(exceptionObserver)
        viewModel.exceptions.observe(viewLifecycleOwner, exceptionObserver)
    }

    private fun filteredLogs(search: String): List<ExceptionEntity> {
        var list = emptyList<ExceptionEntity>()
        viewModel.exceptions.value?.let {
            list = it.filter { exception ->
                (exception.data.exception.name ?: "").contains(search, true) ||
                    (exception.data.exception.file ?: "").contains(search, true)
            }
        }
        binding.noItemText.text = getString(
            if (search.isNotEmpty()) R.string.pluto___no_search_result else R.string.pluto___no_crashes_text
        )
        binding.noItemText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        return list
    }

    private val exceptionObserver = Observer<List<ExceptionEntity>> {
        crashAdapter.list = filteredLogs(binding.search.text.toString())
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is ExceptionEntity) {
                activity?.hideKeyboard()
                if (data.id != null) {
                    router.navigate(Screens.CrashDetails(CrashDetailsFragment.Data(data.id)))
                } else {
                    context?.toast("invalid crash id")
                }
            }
        }
    }
}
