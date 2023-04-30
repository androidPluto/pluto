package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentFilterBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.ui.list.filter.FilterListAdapter
import com.pluto.plugins.rooms.db.internal.ui.list.filter.FilterListItemHolder.Companion.ACTION_DELETE
import com.pluto.plugins.rooms.db.internal.ui.list.filter.FilterListItemHolder.Companion.ACTION_EDIT
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.setList
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
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.noItemText.setOnDebounceClickListener {
            addCondition()
        }
        binding.add.setOnDebounceClickListener {
            addCondition()
        }
        binding.applyFilter.setOnDebounceClickListener {
        }

        viewModel.filterConfig.removeObserver(filterConfigObserver)
        viewModel.filterConfig.observe(viewLifecycleOwner, filterConfigObserver)
    }

    private fun addCondition() {
        // todo open add condition bs
    }

    private val filterConfigObserver = Observer<List<FilterModel>> {
        binding.list.setList(it)
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is FilterModel) {
                when (action) {
                    ACTION_DELETE -> {}
                    ACTION_EDIT -> {}
                }
            }
        }
    }
}
