package com.pluto.plugins.rooms.db.internal.ui.filter

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsDialogAddFilterConditionBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.FilterRelation
import com.pluto.plugins.rooms.db.internal.ui.filter.value.ValueStubFactory
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.BaseValueStub
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import java.lang.IllegalStateException

internal class AddFilterConditionDialog(
    context: Context,
    onAction: (FilterModel) -> Unit
) : BottomSheetDialog(context, R.style.PlutoRoomsDBBottomSheetDialog) {

    private var valueStub: BaseValueStub? = null
    private val sheetView: View = context.inflate(R.layout.pluto_rooms___dialog_add_filter_condition)
    private val binding = PlutoRoomsDialogAddFilterConditionBinding.bind(sheetView)
    private var data: FilterModel? = null
    private var deviceInfo = Device(context)
    private var relation: FilterRelation = FilterRelation.Equals
    private var isFirstRefresh: Boolean = false
    private var isEdit: Boolean = false

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
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            data?.let { filter ->
                isFirstRefresh = true
                binding.column.text = filter.column.name
                updateUi(filter.relation, filter.column, data?.value)
                binding.cta.setOnDebounceClickListener {
                    try {
                        onAction.invoke(
                            FilterModel(
                                column = filter.column,
                                relation = relation,
                                value = valueStub?.getValue()
                            )
                        )
                        dismiss()
                    } catch (e: IllegalStateException) {
                        context.toast(e.message ?: "")
                    }
                }
                binding.relation.setOnDebounceClickListener {
                    ChooseRelationDialog(context, filter.column) {
                        updateUi(it, filter.column)
                    }.show()
                }
            }
        }
    }

    private fun updateUi(relation: FilterRelation, column: ColumnModel, value: String? = null) {
        if (isFirstRefresh || this.relation != relation) {
            this.relation = relation
            binding.relation.setSpan {
                append(bold(fontColor(relation.symbol, context.color(R.color.pluto___text_dark_80))))
                append(italic(light("\t(${relation.javaClass.simpleName})")))
            }
            refreshValueView(relation, column, value)
            isFirstRefresh = false
        }
    }

    private fun refreshValueView(relation: FilterRelation, column: ColumnModel, value: String?) {
        binding.valueStub.getChildAt(0)?.let { binding.valueStub.removeView(it) }
        valueStub = ValueStubFactory.getStub(context, relation, column).apply {
            if (isFirstRefresh && isEdit) {
                setValue(value)
            }
        }
        binding.valueStub.addView(valueStub)
    }

    fun add(data: FilterModel) {
        this.data = data
        this.isEdit = false
        show()
    }

    fun edit(data: FilterModel) {
        this.data = data
        this.isEdit = true
        show()
    }
}
