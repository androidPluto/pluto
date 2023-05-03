package com.pluto.plugins.rooms.db.internal.ui.filter

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsDialogAddFilterConditionBinding
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.FilterRelation
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.setOnDebounceClickListener

internal class AddFilterConditionDialog(
    context: Context,
    onAction: (FilterModel) -> Unit
) : BottomSheetDialog(context, R.style.PlutoRoomsDBBottomSheetDialog) {

    private val sheetView: View = context.inflate(R.layout.pluto_rooms___dialog_add_filter_condition)
    private val binding = PlutoRoomsDialogAddFilterConditionBinding.bind(sheetView)
    private var data: FilterModel? = null
    private var deviceInfo = Device(context)
    private var relation: FilterRelation = FilterRelation.Equals

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
            data?.let { filter ->
                binding.column.text = filter.column.name
                relation = filter.relation
                binding.relation.text = filter.relation.symbol
                binding.value.setText(data?.value)
                binding.cta.setOnDebounceClickListener {
                    onAction.invoke(
                        FilterModel(
                            column = filter.column,
                            relation = relation,
                            value = binding.value.text.toString()
                        )
                    )
                    dismiss()
                }
                binding.relation.setOnDebounceClickListener {
                    ChooseRelationDialog(context, filter.column) {
                        relation = it
                        binding.relation.text = it.symbol
                    }.show()
                }
            }
        }
    }

    fun show(data: FilterModel) {
        this.data = data
        show()
    }
}
