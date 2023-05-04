package com.pluto.plugins.rooms.db.internal.ui.filter

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsDialogChooseRelationBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.FilterRelation
import com.pluto.plugins.rooms.db.internal.ui.filter.list.relation.FilterRelationListAdapter
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.extensions.setList
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ChooseRelationDialog(
    context: Context,
    columnModel: ColumnModel,
    onAction: (FilterRelation) -> Unit
) : BottomSheetDialog(context, R.style.PlutoRoomsDBBottomSheetDialog) {

    private val sheetView: View = context.inflate(R.layout.pluto_rooms___dialog_choose_relation)
    private val binding = PlutoRoomsDialogChooseRelationBinding.bind(sheetView)
    private val deviceInfo = Device(context)

    init {
        setContentView(sheetView)
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
            binding.list.apply {
                adapter = FilterRelationListAdapter(onActionListener)
                addItemDecoration(CustomItemDecorator(context, 12f.dp.toInt()))
            }
            binding.list.setList(
                mutableListOf<FilterRelation>().apply {
                    add(FilterRelation.Equals)
                    add(FilterRelation.NotEquals)
                    add(FilterRelation.Like)
                    if (columnModel.isNumericalType()) {
                        add(FilterRelation.GreaterThan)
                        add(FilterRelation.GreaterThanOrEquals)
                        add(FilterRelation.LessThan)
                        add(FilterRelation.LessThanOrEquals)
                        add(FilterRelation.Between)
                    }
                    add(FilterRelation.In)
                }
            )
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is FilterRelation) {
                onAction.invoke(data)
                dismiss()
            }
        }
    }
}

private fun ColumnModel.isNumericalType(): Boolean {
    return type.lowercase() == "integer" || type.lowercase() == "boolean" || type.lowercase() == "float"
}
