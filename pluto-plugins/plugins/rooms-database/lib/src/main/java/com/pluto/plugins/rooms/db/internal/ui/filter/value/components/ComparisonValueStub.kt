package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueComparisonBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

internal class ComparisonValueStub(context: Context, column: ColumnModel) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueComparisonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.value.inputType = when (column.type.lowercase()) {
            "integer",
            "boolean" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

            "float" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
            else -> InputType.TYPE_CLASS_TEXT
        }
        setValue("")
    }

    @Throws(IllegalStateException::class)
    override fun getValue(): String? {
        if (binding.value.text.trim().isEmpty()) {
            throw IllegalStateException("value cannot be empty")
        }
        return binding.value.text.toString()
    }

    override fun setValue(value: String?) {
        binding.value.setText(value)
    }
}
