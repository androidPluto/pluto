package com.pluto.plugins.rooms.db.internal.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentFilterBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.ui.filter.list.filter.FilterListAdapter
import com.pluto.plugins.rooms.db.internal.ui.filter.list.filter.FilterListItemHolder.Companion.ACTION_DELETE
import com.pluto.plugins.rooms.db.internal.ui.filter.list.filter.FilterListItemHolder.Companion.ACTION_EDIT
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class FilterFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentFilterBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()
    private val filterAdapter: BaseAdapter by autoClearInitializer { FilterListAdapter(onActionListener) }
    private val addFilterConditionDialog: AddFilterConditionDialog by autoClearInitializer {
        AddFilterConditionDialog(requireContext()) {
            addCondition(it)
        }
    }
    private val filterList = arrayListOf<FilterModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_filter, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        binding.list.apply {
            adapter = filterAdapter
            addItemDecoration(CustomItemDecorator(requireContext(), 16f.dp.toInt()))
        }
        binding.noItemText.setOnDebounceClickListener {
            openColumnChooser()
        }
        binding.add.setOnDebounceClickListener {
            openColumnChooser()
        }
        binding.applyFilter.setOnDebounceClickListener {
            viewModel.updateFilter(filterList)
            dismiss()
        }
        filterList.addAll(viewModel.filters)
        filterAdapter.list = filterList
        binding.noItemText.isVisible = filterList.isEmpty()
    }

    private fun openColumnChooser() {
        ChooseColumnForFilterDialog(this, viewModel.processedTableContent, viewModel.filters) {
            addFilterConditionDialog.show(FilterModel(it, null))
        }.show()
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is FilterModel) {
                when (action) {
                    ACTION_DELETE -> deleteCondition(data)
                    ACTION_EDIT -> addFilterConditionDialog.show(data)
                }
            }
        }
    }

    private fun addCondition(data: FilterModel) {
        if (filterList.contains(data)) {
            filterList.remove(data)
        }
        filterList.add(data)
        filterAdapter.notifyDataSetChanged()
        binding.noItemText.isVisible = filterList.isEmpty()
    }

    private fun deleteCondition(data: FilterModel) {
        filterList.remove(data)
        filterAdapter.notifyDataSetChanged()
        binding.noItemText.isVisible = filterList.isEmpty()
    }
}
