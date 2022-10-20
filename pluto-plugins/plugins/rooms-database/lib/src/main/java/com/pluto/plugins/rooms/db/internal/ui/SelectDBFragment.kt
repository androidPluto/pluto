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
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.Session
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDbSelectorBinding
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.RoomsDBViewModel
import com.pluto.plugins.rooms.db.internal.ui.DetailsFragment.Companion.DB_CLASS
import com.pluto.plugins.rooms.db.internal.ui.DetailsFragment.Companion.DB_NAME
import com.pluto.plugins.rooms.db.internal.ui.list.database.DBListAdapter
import com.pluto.utilities.extensions.hideKeyboard
import com.pluto.utilities.extensions.linearLayoutManager
import com.pluto.utilities.extensions.setList
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

class SelectDBFragment : Fragment(R.layout.pluto_rooms___fragment_db_selector) {
    private val binding by viewBinding(PlutoRoomsFragmentDbSelectorBinding::bind)
    private val viewModel: RoomsDBViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()
        binding.list.apply {
            adapter = DBListAdapter(onActionListener)
            addItemDecoration(CustomItemDecorator(requireContext()))
        }

        binding.search.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    Session.searchText = it
                    binding.list.setList(filteredDBs(it))
                    if (it.isEmpty()) {
                        binding.list.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.search.setText(Session.searchText)
        viewModel.dbs.removeObserver(dbListObserver)
        viewModel.dbs.observe(viewLifecycleOwner, dbListObserver)

        binding.close.setOnDebounceClickListener {
            activity?.finish()
        }
    }

    private fun filteredDBs(search: String): List<DatabaseModel> {
        var list = emptyList<DatabaseModel>()
        viewModel.dbs.value?.let {
            list = it.filter { pref ->
                pref.name.contains(search, true)
            }
        }
        binding.noItemText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        return list
    }

    private val dbListObserver = Observer<List<DatabaseModel>> {
        binding.list.setList(filteredDBs(binding.search.text.toString()))
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is DatabaseModel) {
                activity?.let {
                    it.hideKeyboard(viewLifecycleOwner.lifecycleScope) {
                        val bundle = bundleOf(DB_CLASS to data.dbClass, DB_NAME to data.name)
                        findNavController().navigate(R.id.openDetails, bundle)
                    }
                }
            }
        }
    }
}
