package com.pluto.plugins.rooms.db.internal.ui.filter

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsDialogChooseColumnForFilterBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.ProcessedTableContents
import com.pluto.plugins.rooms.db.internal.ui.filter.list.column.ColumnForFilterListAdapter
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.extensions.setList
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ChooseColumnForFilterDialog(
    fragment: Fragment,
    processedTableContent: LiveData<ProcessedTableContents>,
    filterConfig: List<FilterModel>?,
    onAction: (ColumnModel) -> Unit
) : BottomSheetDialog(fragment.requireContext(), R.style.PlutoRoomsDBBottomSheetDialog) {

    private val sheetView: View = context.inflate(R.layout.pluto_rooms___dialog_choose_column_for_filter)
    private val binding = PlutoRoomsDialogChooseColumnForFilterBinding.bind(sheetView)
    private var columns: List<ColumnModel>? = null
    private val deviceInfo = Device(fragment.requireContext())

    init {
        setContentView(sheetView)
        columns = processedTableContent.value?.first
        setOnShowListener { dialog ->
            if (dialog is BottomSheetDialog) {
                val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    isHideable = false
                    skipCollapsed = true
                    peekHeight = deviceInfo.screen.heightPx
                }
            }
            filterConfig?.let {
                binding.list.apply {
                    adapter = ColumnForFilterListAdapter(onActionListener, it)
                    addItemDecoration(CustomItemDecorator(fragment.requireContext(), 12f.dp.toInt()))
                }
                binding.list.setList(processedTableContent.value?.first ?: emptyList())
            }
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is ColumnModel) {
                onAction.invoke(data)
                dismiss()
            }
        }
    }
}
