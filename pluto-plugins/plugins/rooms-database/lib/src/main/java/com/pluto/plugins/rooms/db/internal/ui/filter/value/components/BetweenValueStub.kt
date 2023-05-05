package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueBetweenBinding

internal class BetweenValueStub(context: Context) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueBetweenBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.value1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
        binding.value2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
    }

    override fun getValue(): String? {
        return "${binding.value1.text},${binding.value2.text}"
    }

    override fun setValue(value: String?) {
        binding.value1.setText(value)
    }
}
