package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueStringBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

internal class StringValueStub(context: Context, column: ColumnModel) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueStringBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.value.inputType = when (column.type.lowercase()) {
            "integer",
            "boolean" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

            "float" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
            else -> InputType.TYPE_CLASS_TEXT
        }
    }

    override fun getValue(): String? {
        return binding.value.text.toString()
    }

    override fun setValue(value: String?) {
        binding.value.setText(value)
    }
}
