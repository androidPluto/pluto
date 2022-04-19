package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.drawable
import com.pluto.plugin.utilities.extensions.forEachIndexed
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDataEditorBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.EditEventData
import com.pluto.plugins.rooms.db.internal.core.isSystemTable

class DataEditFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentDataEditorBinding::bind)
//    private val viewModel: RoomsDBDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_data_editor, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    /**
     * Holds all [EditText]s in this view.
     */
    private val etList = mutableListOf<EditText>()

    private val etBackground by lazy {
        requireContext().drawable(R.drawable.pluto_rooms___bg_edittext_round)
    }

    /**
     * The main layout of this view, where all pairs of views will be added.
     */
    private val mainLayout: LinearLayout by lazy {
        LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        convertArguments(arguments)?.let { values ->
            showInsertUI(values)
        } ?: dismiss()
    }

    private fun showInsertUI(dataConfig: EditEventData) {
        dataConfig.values?.let {
            binding.title.text = getString(R.string.pluto_rooms___edit_row_title)
            binding.save.text = getString(R.string.pluto_rooms___edit_cta_text)
        } ?: run {
            binding.title.text = getString(R.string.pluto_rooms___add_row_title)
            binding.save.text = getString(R.string.pluto_rooms___add_cta_text)
        }
        binding.warning.visibility = if (isSystemTable(dataConfig.table)) VISIBLE else GONE
        val columns = dataConfig.columns
        val rows = dataConfig.values ?: dataConfig.columns.map { null }
        Pair(columns, rows).forEachIndexed { _, column, value ->
            val columnTextView = getColumnTextView(column)
            val valueEditText = getValueTextView(column, value)
            etList.add(valueEditText)
            val row = LinearLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
                addView(columnTextView)
                addView(valueEditText)
            }
            mainLayout.addView(row)
        }
        binding.nsv.removeAllViews()
        binding.nsv.addView(mainLayout)
    }

    private fun getValueTextView(column: ColumnModel, value: String?): EditText = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1f
            setMargins(0, 4f.dp.toInt(), 0, 8f.dp.toInt())
        }
        setPadding(
            paddingStart + 8f.dp.toInt(),
            paddingTop,
            paddingEnd + 8f.dp.toInt(),
            paddingBottom
        )
        hint = "${column.type} (${if (column.isNotNull) "not_null" else "null"})"
        inputType = handleKeypad(column.type)
        textSize = TEXT_SIZE_DATA
        setTextColor(requireContext().color(R.color.pluto___text_dark_80))
        setHintTextColor(requireContext().color(R.color.pluto___text_dark_20))
        typeface = ResourcesCompat.getFont(context, R.font.muli_semibold)
        background = etBackground
        setText(value ?: column.defaultValue?.replace("\'", ""))
    }

    // todo increase data type coverage
    private fun handleKeypad(type: String): Int = when (type) {
        "INTEGER" -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
        else -> InputType.TYPE_CLASS_TEXT
    }

    private fun getColumnTextView(column: ColumnModel): TextView = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(4f.dp.toInt(), 8f.dp.toInt(), 0, 0)
        }
        setSpan {
            append(semiBold(column.name))
            if (column.isPrimaryKey) {
                append(light("\t(PRIMARY KEY)"))
            }
        }
        textSize = TEXT_SIZE_COLUMN
        setTextColor(requireContext().color(R.color.pluto___text_dark_60))
    }

    private fun convertArguments(arguments: Bundle?): EditEventData? {
        return arguments?.getParcelable("data")
    }

    companion object {
        const val TEXT_SIZE_COLUMN = 14f
        const val TEXT_SIZE_DATA = 15f
    }
}
