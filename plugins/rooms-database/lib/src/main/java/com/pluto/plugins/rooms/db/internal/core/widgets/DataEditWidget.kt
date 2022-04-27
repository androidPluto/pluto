package com.pluto.plugins.rooms.db.internal.core.widgets

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsDataEditWidgetBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

internal class DataEditWidget(context: Context) : ConstraintLayout(context) {

    private val binding = PlutoRoomsDataEditWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    var content: String? = null
        get() {
            return null
        }
        internal set

    private var isNull: Boolean = false

    init {
        binding.nullCta.setDebounceClickListener {
            if (isNull) {
                isNull = false
            } else {
                isNull = true
                content = null
                binding.value.text = null
            }
        }
    }

    fun setup(column: ColumnModel) {
        binding.value.hint = "${column.type} (${if (column.isNotNull) "not_null" else "null"})"
        binding.value.inputType = handleKeypad(column.type)
        binding.value.setText(column.defaultValue?.replace("\'", ""))
    }

    // todo increase data type coverage
    private fun handleKeypad(type: String): Int = when (type) {
        "INTEGER" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
        else -> InputType.TYPE_CLASS_TEXT
    }

//    init {
// //        initView()
//    }
//
//    private fun initView() {
// //        inflate(context, R.layout.pluto_rooms___data_edit_widget, this);
//    }
}
