package com.pluto.plugins.preferences.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.preferences.R
import com.pluto.plugins.preferences.SharedPrefRepo
import com.pluto.plugins.preferences.databinding.PlutoPrefFragmentFilterBinding
import com.pluto.plugins.preferences.getSharePreferencesFiles
import com.pluto.plugins.preferences.ui.SharedPrefAdapter
import com.pluto.plugins.preferences.ui.SharedPrefFile
import com.pluto.plugins.preferences.ui.SharedPrefViewModel
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class FilterFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoPrefFragmentFilterBinding::bind)
    private val viewModel: SharedPrefViewModel by activityViewModels()

    private val prefAdapter: BaseAdapter by autoClearInitializer { SharedPrefAdapter(onActionListener) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_pref___fragment_filter, container, false)

    override fun getTheme(): Int = R.style.PlutoPrefBottomSheetDialog

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
            adapter = prefAdapter
            addItemDecoration(CustomItemDecorator(requireContext(), DECORATOR_DIVIDER_PADDING))
        }
        binding.doneCta.setOnDebounceClickListener {
            findNavController().navigateUp()
        }

        binding.clear.setOnDebounceClickListener {
            SharedPrefRepo.deSelectAll()
            prefAdapter.notifyDataSetChanged()
            requireContext().toast(requireContext().getString(R.string.pluto_pref___preferences_cleared))
        }

        prefAdapter.list = context?.getSharePreferencesFiles() ?: emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.refresh()
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is SharedPrefFile) {
                SharedPrefRepo.updateSelectedPreferenceFile(data)
                prefAdapter.notifyItemChanged(holder.layoutPosition)
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
