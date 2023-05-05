package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.view.LayoutInflater
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueInBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

@SuppressWarnings("UnusedPrivateMember")
/**
 * todo need to solve input type integer with comma(,)
 */
internal class InValueStub(context: Context, column: ColumnModel) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueInBinding.inflate(LayoutInflater.from(context), this, true)

    override fun getValue(): String? {
        return binding.value.text.toString()
    }

    override fun setValue(value: String?) {
        binding.value.setText(value)
    }
}
