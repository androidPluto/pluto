package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.hideKeyboard
import com.pluto.plugin.utilities.extensions.linearLayoutManager
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.Session
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDbSelectorBinding
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.RoomsDBViewModel
import com.pluto.plugins.rooms.db.internal.ui.DBDetailsFragment.Companion.DB_CLASS
import com.pluto.plugins.rooms.db.internal.ui.DBDetailsFragment.Companion.DB_NAME

class DBSelectorFragment : Fragment(R.layout.pluto_rooms___fragment_db_selector) {
    private val binding by viewBinding(PlutoRoomsFragmentDbSelectorBinding::bind)
    private val viewModel: RoomsDBViewModel by activityViewModels()

    private val prefAdapter: BaseAdapter by lazy { RoomsDBAdapter(onActionListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()
        binding.list.apply {
            adapter = prefAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }

        binding.search.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    Session.searchText = it
                    prefAdapter.list = filteredDBs(it)
                    if (it.isEmpty()) {
                        binding.list.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.search.setText(Session.searchText)
        viewModel.dbs.removeObserver(sharedPrefObserver)
        viewModel.dbs.observe(viewLifecycleOwner, sharedPrefObserver)

        binding.close.setDebounceClickListener {
            activity?.finish()
        }
    }

    private fun filteredDBs(search: String): List<DatabaseModel> {
        var list = emptyList<DatabaseModel>()
        viewModel.dbs.value?.let {
            list = it.filter { pref ->
                pref.dbName.contains(search, true)
            }
        }
        binding.noItemText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        return list
    }

    private val sharedPrefObserver = Observer<List<DatabaseModel>> {
        prefAdapter.list = filteredDBs(binding.search.text.toString())
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is DatabaseModel) {
                activity?.let {
                    it.hideKeyboard(viewLifecycleOwner.lifecycleScope) {
                        val bundle = bundleOf(DB_CLASS to data.dbClass, DB_NAME to data.dbName)
                        findNavController().navigate(R.id.openDetails, bundle)
                    }
                }
            }
        }
    }
}
