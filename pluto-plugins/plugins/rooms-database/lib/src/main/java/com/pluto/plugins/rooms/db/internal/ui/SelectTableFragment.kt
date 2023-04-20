package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentTableSelectorBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_FETCH_TABLES
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.plugins.rooms.db.internal.ui.list.table.TableListAdapter
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.setList
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.viewBinding
import java.lang.Exception

internal class SelectTableFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentTableSelectorBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_table_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = TableListAdapter(onActionListener)
            addItemDecoration(CustomItemDecorator(requireContext(), 16f.dp.toInt()))
        }

        viewModel.tables.removeObserver(tableListObserver)
        viewModel.tables.observe(viewLifecycleOwner, tableListObserver)

        viewModel.queryError.removeObserver(errorObserver)
        viewModel.queryError.observe(viewLifecycleOwner, errorObserver)
    }

    private val tableListObserver = Observer<List<TableModel>> {
        binding.list.setList(it)
    }

    private val errorObserver = Observer<Pair<String, Exception>> {
        handleError(it.first, it.second)
    }

    private fun handleError(error: String, exception: Exception) {
        when (error) {
            ERROR_FETCH_TABLES -> {
                toast("Exception occurred (see logs) : $exception")
                exception.printStackTrace()
            }
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is TableModel) {
                viewModel.selectTable(data)
                dismiss()
            }
        }
    }
}
