package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDataEditorBinding
import com.pluto.plugins.rooms.db.internal.ValuesModel

class DataEditFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentDataEditorBinding::bind)
//    private val viewModel: RoomsDBDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_data_editor, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        convertArguments(arguments)?.let { dataConfig ->
            dataConfig.values?.let {
                showUpdateUI(dataConfig)
            } ?: showInsertUI(dataConfig)
        } ?: dismiss()
    }

    private fun showInsertUI(dataConfig: ValuesModel) {
        binding.title.text = getString(R.string.pluto_rooms___add_row_title)
        toast("insert ${dataConfig.index}")
    }

    private fun showUpdateUI(dataConfig: ValuesModel) {
        binding.title.text = getString(R.string.pluto_rooms___edit_row_title)
        toast("update ${dataConfig.index}")
    }

    private fun convertArguments(arguments: Bundle?): ValuesModel? {
        return arguments?.let {
            val index = it.getInt(DATA_INDEX)
            val values = it.getStringArrayList(DATA_VALUES)
            it.getStringArrayList(DATA_COLUMNS)?.let { columns ->
                ValuesModel(index, columns, values)
            }
        }
    }

    companion object {
        const val DATA_INDEX = "index"
        const val DATA_COLUMNS = "columns"
        const val DATA_VALUES = "values"
    }
}
