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

    @Throws(IllegalStateException::class)
    override fun getValue(): String? {
        if (binding.value1.text.trim().isEmpty() || binding.value2.text.trim().isEmpty()) {
            throw IllegalStateException("values cannot be empty")
        }
        return "${binding.value1.text.trim()},${binding.value2.text}"
    }

    override fun setValue(value: String?) {
        val split = value?.split(",")
        if (!split.isNullOrEmpty()) {
            binding.value1.setText(split[0].trim())
            binding.value2.setText(split[1].trim())
        }
    }
}
