package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentTableSelectorBinding
import com.pluto.plugins.rooms.db.internal.RoomsDBDetailsViewModel
import com.pluto.plugins.rooms.db.internal.TableModel

class TableSelectorFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentTableSelectorBinding::bind)
    private val viewModel: RoomsDBDetailsViewModel by activityViewModels()

    private val tableAdapter: BaseAdapter by lazy { RoomsTableAdapter(onActionListener) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_table_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = tableAdapter
            addItemDecoration(CustomItemDecorator(requireContext(), 16f.dp.toInt()))
        }

        viewModel.tables.removeObserver(tableListObserver)
        viewModel.tables.observe(viewLifecycleOwner, tableListObserver)
    }

    private val tableListObserver = Observer<Pair<List<TableModel>, Exception?>> {
        it.second?.let {
            // todo capture exception
        } ?: processTableList(it.first)
    }

    private fun processTableList(list: List<TableModel>) {
        tableAdapter.list = list
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is TableModel) {
                viewModel.selectTable(data)
                dismiss()
            }
        }
    }
}
