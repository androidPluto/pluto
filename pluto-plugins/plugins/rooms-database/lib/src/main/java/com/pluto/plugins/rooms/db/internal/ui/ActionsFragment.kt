package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentRowActionsBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.RowAction
import com.pluto.plugins.rooms.db.internal.RowDetailsData
import com.pluto.utilities.device.Device
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class ActionsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentRowActionsBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_row_actions, container, false)

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
        convertArguments(arguments)?.let { dataConfig ->
            setupView(dataConfig)
        } ?: dismiss()
    }

    private fun setupView(dataConfig: RowDetailsData) {
        binding.delete.setOnDebounceClickListener {
            viewModel.performAction(RowAction.Delete, dataConfig)
            dismiss()
        }
        binding.duplicate.setOnDebounceClickListener {
            viewModel.performAction(RowAction.Duplicate, dataConfig)
            dismiss()
        }
    }

    private fun convertArguments(arguments: Bundle?): RowDetailsData? {
        return arguments?.getParcelable("data")
    }
}
