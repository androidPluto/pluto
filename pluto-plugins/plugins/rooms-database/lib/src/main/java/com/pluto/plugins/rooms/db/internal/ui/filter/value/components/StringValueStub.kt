package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.view.LayoutInflater
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueStringBinding

internal class StringValueStub(context: Context) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueStringBinding.inflate(LayoutInflater.from(context), this, true)

    override fun getValue(): String? {
        return binding.value.text.toString()
    }

    override fun setValue(value: String?) {
        binding.value.setText(value)
    }
}
