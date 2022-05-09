package com.pluto.plugins.rooms.db.internal.core.widgets

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsDataEditWidgetBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel

internal class DataEditWidget(context: Context) : ConstraintLayout(context) {

    private val binding = PlutoRoomsDataEditWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    var input: Pair<ColumnModel, String?>? = null

    fun get(): Pair<ColumnModel, String?> {
        input?.let {
            return Pair(it.first, if (isNull) null else binding.value.text.toString())
        }
        throw IllegalStateException("calling get before creating the widget")
    }

    private var isNull: Boolean = false

    fun create(column: ColumnModel, value: String?) {
        input = Pair(column, value)
        binding.column.text = context.createSpan {
            if (column.isPrimaryKey) {
                append(semiBold(underline(column.name)))
                append(fontColor(italic(" (Primary Key)"), context.color(R.color.pluto___text_dark_40)))
            } else {
                append(column.name)
            }

            if (!column.isNotNull) {
                append(fontColor(italic(" (nullable)"), context.color(R.color.pluto___text_dark_40)))
            }
        }
        binding.nullCta.visibility = if (column.isNotNull) GONE else VISIBLE
        setValue(value)
        binding.value.inputType = handleKeypad(column.type)
        binding.nullCta.setOnLongClickListener {
            context?.toast(context.getString(R.string.pluto_rooms___set_as_null))
            true
        }
        binding.nullCta.setDebounceClickListener {
            if (!column.isNotNull) {
                setValue(null)
            }
        }
        binding.value.doOnTextChanged { text, _, _, _ ->
            if (text?.length ?: 0 > 0) {
                isNull = false
                binding.value.hint = context?.createSpan {
                    append("blank")
                }
            }
        }
    }

    private fun setValue(value: String?) {
        isNull = value == null
        binding.value.hint = context?.createSpan {
            if (isNull) {
                append(italic("null"))
            } else {
                append("blank")
            }
        }
        binding.value.setText(value)
    }

    // todo increase data type coverage
    private fun handleKeypad(type: String): Int = when (type) {
        "INTEGER" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
        else -> InputType.TYPE_CLASS_TEXT
    }
}
