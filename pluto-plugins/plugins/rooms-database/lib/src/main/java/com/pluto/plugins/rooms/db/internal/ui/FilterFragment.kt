package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.PlutoRoomsDBWatcher
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentFilterBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.RowDetailsData
import com.pluto.plugins.rooms.db.internal.core.query.ExecuteResult
import com.pluto.utilities.DebugLog
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class FilterFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentFilterBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_filter, container, false)

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
        convertArguments(arguments).apply {
            convertArguments(arguments).second?.let {
            }
        }
        binding.add.setOnDebounceClickListener {
        }

        viewModel.editEventState.removeObserver(editStateObserver)
        viewModel.editEventState.observe(viewLifecycleOwner, editStateObserver)

        viewModel.editError.removeObserver(errorObserver)
        viewModel.editError.observe(viewLifecycleOwner, errorObserver)
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
            ContentViewModel.ERROR_ADD_UPDATE -> {
                findNavController().navigate(
                    R.id.openQueryErrorDialog,
                    bundleOf(QueryErrorFragment.ERROR_MESSAGE to exception.message)
                )
                DebugLog.e(PlutoRoomsDBWatcher.LOG_TAG, "error while editing the table", exception)
            }
        }
    }

    private fun convertArguments(arguments: Bundle?): Pair<Boolean, RowDetailsData?> {
        return Pair(arguments?.getBoolean("isInsert") ?: true, arguments?.getParcelable("data"))
    }
}
