package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentColumnDetailsBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.SortBy
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class ColumnDetailsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentColumnDetailsBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_column_details, container, false)

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
        convertArguments(arguments)?.let { data ->
            setupView(data)
        } ?: dismiss()
    }

    private fun setupView(data: Pair<TableModel, ColumnModel>) {
        binding.title.text = data.second.name
        binding.primaryKey.visibility = if (data.second.isPrimaryKey) VISIBLE else GONE
        viewModel.sortBy?.let {
            if (data.second.name.lowercase() == it.first.lowercase()) {
                when (it.second) {
                    is SortBy.Asc ->
                        binding.sortAscLabel.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.pluto_rooms___bg_sort_option_badge_selected)
                    is SortBy.Desc ->
                        binding.sortDescLabel.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.pluto_rooms___bg_sort_option_badge_selected)
                }
            }
        }
        binding.sortAscLabel.setOnDebounceClickListener(haptic = true) {
            applySort(data, SortBy.Asc())
        }

        binding.sortDescLabel.setOnDebounceClickListener(haptic = true) {
            applySort(data, SortBy.Desc())
        }

        binding.sortClear.setOnDebounceClickListener(haptic = true) {
            viewModel.clearSortBy()
            viewModel.selectTable(data.first)
            dismiss()
        }
    }

    private fun applySort(data: Pair<TableModel, ColumnModel>, sort: SortBy) {
        toast("Sorting by ${data.second.name} : ${sort.label}")
        viewModel.setSortBy(data.second, sort)
        viewModel.selectTable(data.first)
        dismiss()
    }

    private fun convertArguments(arguments: Bundle?): Pair<TableModel, ColumnModel>? {
        val table = arguments?.getParcelable<TableModel>(ATTR_TABLE)
        val column = arguments?.getParcelable<ColumnModel>(ATTR_COLUMN)
        return if (table != null && column != null) {
            Pair(table, column)
        } else {
            null
        }
    }

    companion object {
        const val ATTR_COLUMN = "column"
        const val ATTR_TABLE = " table"
    }
}
