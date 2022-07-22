package com.pluto.plugins.preferences.ui.filter

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.preferences.R
import com.pluto.plugins.preferences.SharedPrefRepo
import com.pluto.plugins.preferences.databinding.PlutoPrefFragmentFilterBinding
import com.pluto.plugins.preferences.getSharePreferencesFiles
import com.pluto.plugins.preferences.ui.SharedPrefAdapter
import com.pluto.plugins.preferences.ui.SharedPrefFile
import com.pluto.plugins.preferences.ui.SharedPrefViewModel

internal class FilterFragment : Fragment(R.layout.pluto_pref___fragment_filter) {

    private val binding by viewBinding(PlutoPrefFragmentFilterBinding::bind)
    private val viewModel: SharedPrefViewModel by activityViewModels()

    private val prefAdapter: BaseAdapter by lazy { SharedPrefAdapter(onActionListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
        binding.list.apply {
            adapter = prefAdapter
            addItemDecoration(CustomItemDecorator(requireContext(), DECORATOR_DIVIDER_PADDING))
        }
        binding.back.setOnDebounceClickListener {
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
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is SharedPrefFile) {
                SharedPrefRepo.updateSelectedPreferenceFile(data)
                holder?.adapterPosition?.let { prefAdapter.notifyItemChanged(it) }
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
