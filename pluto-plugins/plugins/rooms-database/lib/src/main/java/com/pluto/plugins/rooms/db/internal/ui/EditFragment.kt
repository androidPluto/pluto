package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugin.utilities.extensions.forEachIndexed
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.sharing.ContentShareViewModel
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.PlutoRoomsDBWatcher.LOG_TAG
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDataEditorBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_ADD_UPDATE
import com.pluto.plugins.rooms.db.internal.RowDetailsData
import com.pluto.plugins.rooms.db.internal.UIViewModel
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.ExecuteResult
import com.pluto.plugins.rooms.db.internal.core.widgets.DataEditWidget

class EditFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentDataEditorBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by viewModels()
    private val sharer: ContentShareViewModel by lazyContentSharer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_data_editor, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    /**
     * Holds all [EditText]s in this view.
     */
    private val etList = mutableListOf<DataEditWidget>()
    private val fieldValues
        get() = etList.map { it.get() }

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
        convertArguments(arguments).apply {
            convertArguments(arguments).second?.let {
                showInsertUI(this.first, it)
            }
        }

        viewModel.editEventState.removeObserver(editStateObserver)
        viewModel.editEventState.observe(viewLifecycleOwner, editStateObserver)

        viewModel.editError.removeObserver(errorObserver)
        viewModel.editError.observe(viewLifecycleOwner, errorObserver)

        uiViewModel.rowEditView.removeObserver(uiObserver)
        uiViewModel.rowEditView.observe(viewLifecycleOwner, uiObserver)
    }

    private val uiObserver = Observer<Pair<List<DataEditWidget>, LinearLayout>> {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.loader.visibility = GONE
            etList.clear()
            etList.addAll(it.first)

            binding.nsv.removeAllViews()
            binding.nsv.addView(it.second)
        }
    }

    private fun showInsertUI(isInsertEvent: Boolean, dataConfig: RowDetailsData) {
        if (isInsertEvent) {
            binding.title.text = getString(R.string.pluto_rooms___add_row_title)
            binding.save.text = getString(R.string.pluto_rooms___add_cta_text)
            binding.share.visibility = GONE
        } else {
            binding.title.text = getString(R.string.pluto_rooms___edit_row_title)
            binding.save.text = getString(R.string.pluto_rooms___edit_cta_text)
            binding.share.visibility = VISIBLE
        }
        binding.warning.visibility = if (isSystemTable(dataConfig.table)) VISIBLE else GONE
        uiViewModel.generateRowEditView(requireContext(), dataConfig)
        binding.share.setOnDebounceClickListener {
            sharer.share(
                Shareable(
                    title = "Share Row",
                    content = dataConfig.toShareText()
                )
            )
        }
        binding.save.setOnDebounceClickListener {
            if (isInsertEvent) {
                viewModel.addNewRow(dataConfig.table, fieldValues)
            } else {
                dataConfig.values?.let { values ->
                    val prevValues: ArrayList<Pair<ColumnModel, String?>> = arrayListOf()
                    fieldValues.forEachIndexed { index, pair ->
                        prevValues.add(Pair(pair.first, values[index]))
                    }
                    viewModel.updateRow(dataConfig.table, fieldValues, prevValues)
                }
            }
        }
    }

    private val editStateObserver = Observer<ExecuteResult.Success> {
        when (it) {
            is ExecuteResult.Success.Insert -> toast("item id ${it.id} inserted!")
            is ExecuteResult.Success.Update -> toast("${it.numberOfRows} row updated!")
            else -> { /*ignore*/
            }
        }
        dismiss()
    }

    private val errorObserver = Observer<Pair<String, Exception>> {
        handleError(it.first, it.second)
    }

    private fun handleError(error: String, exception: Exception) {
        when (error) {
            ERROR_ADD_UPDATE -> {
                toast("Error (see logs):\n${exception.message}")
                DebugLog.e(LOG_TAG, "error while editing the table", exception)
            }
        }
    }

    private fun convertArguments(arguments: Bundle?): Pair<Boolean, RowDetailsData?> {
        return Pair(arguments?.getBoolean("isInsert") ?: true, arguments?.getParcelable("data"))
    }
}

private fun RowDetailsData.toShareText(): String {
    val text = StringBuilder()
    text.append("Row from table: $table\n")
    text.append("{\n")
    values?.let {
        Pair(columns, it).forEachIndexed { _, column, value ->
            text.append("\t${column.name}: $value\n")
        }
    }
    text.append("}")
    return text.toString()
}
