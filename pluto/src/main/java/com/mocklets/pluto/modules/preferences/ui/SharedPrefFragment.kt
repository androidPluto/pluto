package com.mocklets.pluto.modules.preferences.ui

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
import com.mocklets.pluto.core.extensions.delayedLaunchWhenResumed
import com.mocklets.pluto.core.extensions.linearLayoutManager
import com.mocklets.pluto.core.extensions.showMoreOptions
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.CustomItemDecorator
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.routing.Screens
import com.mocklets.pluto.core.ui.routing.lazyRouter
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoFragmentSharedPrefBinding
import com.mocklets.pluto.modules.preferences.SharedPrefRepo

internal class SharedPrefFragment : Fragment(R.layout.pluto___fragment_shared_pref) {

    private val binding by viewBinding(PlutoFragmentSharedPrefBinding::bind)
    private val viewModel: SharedPrefViewModel by activityViewModels()
    private lateinit var editDialog: SharedPrefEditDialog

    private val prefAdapter: BaseAdapter by lazy { SharedPrefAdapter(onActionListener) }
    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refresh()
        binding.list.apply {
            adapter = prefAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        editDialog = SharedPrefEditDialog(this) { pair, value ->
            SharedPrefRepo.set(requireContext(), pair, value)
            lifecycleScope.delayedLaunchWhenResumed(DIALOG_DISMISS_DELAY) {
                viewModel.refresh()
                editDialog.dismiss()
            }
        }
//        filterDialog = SharedPrefFilterDilog(this)
        binding.search.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
                    Pluto.session.preferencesSearchText = it
                    prefAdapter.list = filteredPrefs(it)
                    if (it.isEmpty()) {
                        binding.list.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.more.setDebounceClickListener {
            requireContext().showMoreOptions(it, R.menu.pluto___popup_menu_shared_pref) { item ->
                when (item.itemId) {
                    R.id.filter -> router.navigate(Screens.SharedPrefFilter)
                }
            }
        }
        binding.search.setText(Pluto.session.preferencesSearchText)
        viewModel.preferences.removeObserver(sharedPrefObserver)
        viewModel.preferences.observe(viewLifecycleOwner, sharedPrefObserver)
    }

    private fun filteredPrefs(search: String): List<SharedPrefKeyValuePair> {
        var list = emptyList<SharedPrefKeyValuePair>()
        viewModel.preferences.value?.let {
            list = it.filter { pref ->
                pref.key.contains(search, true)
            }
        }
        binding.noItemText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        return list
    }

    private val sharedPrefObserver = Observer<List<SharedPrefKeyValuePair>> {
        prefAdapter.list = filteredPrefs(binding.search.text.toString())
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is SharedPrefKeyValuePair) {
                editDialog.show(data)
            }
        }
    }

    private companion object {
        const val DIALOG_DISMISS_DELAY = 200L
    }
}
