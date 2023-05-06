package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsStubFilterValueStringBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.utilities.setOnDebounceClickListener

internal class StringValueStub(context: Context, private val column: ColumnModel) : BaseValueStub(context) {

    private val binding = PlutoRoomsStubFilterValueStringBinding.inflate(LayoutInflater.from(context), this, true)
    private var isNull: Boolean = false

    init {
        binding.value.inputType = when (column.type.lowercase()) {
            "integer",
            "boolean" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

            "float" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
            else -> InputType.TYPE_CLASS_TEXT
        }
        binding.value.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                isNull = false
                binding.value.hint = BLANK
            }
        }
        binding.nullCta.visibility = if (column.isNotNull) GONE else VISIBLE
        binding.nullCta.setOnDebounceClickListener {
            if (!column.isNotNull) {
                setValue(null)
            }
        }
        setValue("")
    }

    @Throws(IllegalStateException::class)
    override fun getValue(): String? {
        return if (isNull) {
            null
        } else {
            binding.value.text.toString()
        }
    }

    override fun setValue(value: String?) {
        binding.value.setText(value)
        binding.value.hint = BLANK
        if (!column.isNotNull && value == null) {
            isNull = true
            binding.value.hint = NULL
        }
    }

    private companion object {
        const val BLANK = "blank"
        const val NULL = "null"
    }
}
