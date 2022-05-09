package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.setList
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentTableSchemaBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ProcessedTableContents
import com.pluto.plugins.rooms.db.internal.ui.list.column.ColumnListAdapter

class TableSchemaFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentTableSchemaBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_table_schema, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

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

        binding.list.apply {
            adapter = ColumnListAdapter()
            addItemDecoration(CustomItemDecorator(requireContext(), 16f.dp.toInt()))
        }

        viewModel.processedTableContent.removeObserver(tableContentObserver)
        viewModel.processedTableContent.observe(viewLifecycleOwner, tableContentObserver)
    }

    private val tableContentObserver = Observer<ProcessedTableContents> {
        binding.list.setList(it.first)
    }
}
