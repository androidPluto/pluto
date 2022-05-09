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
import com.pluto.plugin.utilities.extensions.setList
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.sharing.ContentShareViewModel
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentTableSchemaBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ProcessedTableContents
import com.pluto.plugins.rooms.db.internal.ui.list.column.ColumnListAdapter

class TableSchemaFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentTableSchemaBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()
    private val sharer: ContentShareViewModel by lazyContentSharer()

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
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.share.setDebounceClickListener {
            viewModel.processedTableContent.value?.first?.let {
                sharer.share(
                    Shareable(
                        title = "Share Row",
                        content = it.toShareText()
                    )
                )
            }
        }

        viewModel.processedTableContent.removeObserver(tableContentObserver)
        viewModel.processedTableContent.observe(viewLifecycleOwner, tableContentObserver)
    }

    private val tableContentObserver = Observer<ProcessedTableContents> {
        binding.list.setList(it.first)
    }
}

private fun List<ColumnModel>.toShareText(): String {
    val text = StringBuilder()
    text.append("Table Schema\n")
    forEach {
        text.append("${it.name}: {\n")
        text.append("\tprimary_key: ${it.isPrimaryKey}\n ")
        text.append("\ttype: ${it.type}, ")
        if (it.isNotNull) {
            text.append("NOT_NULL\n")
        } else {
            text.append("NULL\n")
        }
        it.defaultValue?.let { def ->
            text.append("\tdefault_value: $def\n")
        }
        text.append("}\n")
    }
    return text.toString()
}
