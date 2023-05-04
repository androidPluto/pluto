package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.view.LayoutInflater
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueBetweenBinding

internal class BetweenValueStub(context: Context) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueBetweenBinding.inflate(LayoutInflater.from(context), this, true)

    override fun getValue(): String? {
        return binding.value.text.toString()
    }

    override fun setValue(value: String?) {
        binding.value.setText(value)
    }
}
