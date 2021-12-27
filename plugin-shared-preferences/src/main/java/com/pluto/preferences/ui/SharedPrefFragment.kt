package com.pluto.preferences.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.plugin.utilities.extensions.delayedLaunchWhenResumed
import com.mocklets.pluto.plugin.utilities.extensions.linearLayoutManager
import com.mocklets.pluto.plugin.utilities.list.BaseAdapter
import com.mocklets.pluto.plugin.utilities.list.CustomItemDecorator
import com.mocklets.pluto.plugin.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem
import com.mocklets.pluto.plugin.utilities.viewBinding
import com.pluto.preferences.R
import com.pluto.preferences.SharedPrefRepo
import com.pluto.preferences.databinding.PlutoPrefFragmentSharedPrefBinding

internal class SharedPrefFragment : Fragment(R.layout.pluto_pref___fragment_shared_pref) {
    private val binding by viewBinding(PlutoPrefFragmentSharedPrefBinding::bind)
    private val viewModel: SharedPrefViewModel by activityViewModels()
    private lateinit var editDialog: SharedPrefEditDialog

    private val prefAdapter: BaseAdapter by lazy { SharedPrefAdapter(onActionListener) }

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
        binding.search.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
//                    Pluto.session.preferencesSearchText = it
                    prefAdapter.list = filteredPrefs(it)
                    if (it.isEmpty()) {
                        binding.list.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
//        binding.more.setDebounceClickListener {
//            requireContext().showMoreOptions(it, R.menu.pluto___popup_menu_shared_pref) { item ->
//                when (item.itemId) {
//                    R.id.filter -> router.navigate(Screens.SharedPrefFilter)
//                }
//            }
//        }
//        binding.search.setText(Pluto.session.preferencesSearchText)
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
