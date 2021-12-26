package com.pluto.preferences.ui.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mocklets.pluto.utilities.extensions.dp
import com.mocklets.pluto.utilities.extensions.toast
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.CustomItemDecorator
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding
import com.pluto.preferences.R
import com.pluto.preferences.SharedPrefRepo
import com.pluto.preferences.databinding.PlutoPrefFragmentSharedPrefFilterBinding
import com.pluto.preferences.getSharePreferencesFiles
import com.pluto.preferences.ui.SharedPrefAdapter
import com.pluto.preferences.ui.SharedPrefFile
import com.pluto.preferences.ui.SharedPrefViewModel

internal class SharedPrefFilterFragment : Fragment(R.layout.pluto_pref___fragment_shared_pref_filter) {

    private val binding by viewBinding(PlutoPrefFragmentSharedPrefFilterBinding::bind)
    private val viewModel: SharedPrefViewModel by activityViewModels()

    private val prefAdapter: BaseAdapter by lazy { SharedPrefAdapter(onActionListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = prefAdapter
            addItemDecoration(CustomItemDecorator(requireContext(), DECORATOR_DIVIDER_PADDING))
        }
        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }

        binding.clear.setDebounceClickListener {
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
