package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugin.utilities.extensions.forEachIndexed
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDataEditorBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_ADD_UPDATE
import com.pluto.plugins.rooms.db.internal.EditEventData
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.widgets.DataEditWidget

class DataEditFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentDataEditorBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_data_editor, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    /**
     * Holds all [EditText]s in this view.
     */
    private val etList = mutableListOf<DataEditWidget>()
    private val fieldValues
        get() = etList.map { it.get }

//    private val etBackground by lazy {
//        requireContext().drawable(R.drawable.pluto_rooms___bg_edittext_round)
//    }

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
        convertArguments(arguments)?.let { dataConfig ->
            showInsertUI(dataConfig)
        } ?: dismiss()

        viewModel.editEventState.removeObserver(editStateObserver)
        viewModel.editEventState.observe(viewLifecycleOwner, editStateObserver)

        viewModel.error.removeObserver(errorObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun showInsertUI(dataConfig: EditEventData) {
        if (dataConfig.isInsertEvent) {
            binding.title.text = getString(R.string.pluto_rooms___add_row_title)
            binding.save.text = getString(R.string.pluto_rooms___add_cta_text)
        } else {
            binding.title.text = getString(R.string.pluto_rooms___edit_row_title)
            binding.save.text = getString(R.string.pluto_rooms___edit_cta_text)
        }
        binding.warning.visibility = if (isSystemTable(dataConfig.table)) VISIBLE else GONE
        val columns = dataConfig.columns
        val rows = dataConfig.values ?: dataConfig.columns.map { col ->
            col.defaultValue?.replace("\'", "")
        }
        Pair(columns, rows).forEachIndexed { _, column, value ->
            val valueEditText = DataEditWidget(requireContext())
            etList.add(valueEditText)
            valueEditText.create(column, value)
            val row = LinearLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
                addView(valueEditText)
            }
            mainLayout.addView(row)
        }
        binding.nsv.removeAllViews()
        binding.nsv.addView(mainLayout)
        binding.save.setDebounceClickListener {
            fieldValues.forEach {
                it.second?.let { value ->
                    DebugLog.d("prateek", "${it.first.name} : $value")
                }
            }
            if (dataConfig.isInsertEvent) {
//                viewModel.addNewRow(dataConfig.table, fieldValues)
            } else {
                dataConfig.values?.let { values ->
//                    viewModel.updateRow(dataConfig.table, dataConfig.columns.map { it.name }, values, fieldValues)
                }
            }
        }
    }

    private val editStateObserver = Observer<Boolean> {
        toast("Success!")
        dismiss()
    }

    private val errorObserver = Observer<Pair<String, Exception>> {
        handleError(it.first, it.second)
    }

    private fun handleError(error: String, exception: Exception) {
        when (error) {
            ERROR_ADD_UPDATE -> {
                toast("Error (see logs) : ${exception.message}")
                exception.printStackTrace()
            }
        }
    }

    private fun convertArguments(arguments: Bundle?): EditEventData? {
        return arguments?.getParcelable("data")
    }
}
