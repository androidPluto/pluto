package com.mocklets.pluto.modules.preferences.ui.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.dp
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.ui.list.*
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoFragmentSharedPrefFilterBinding
import com.mocklets.pluto.modules.preferences.SharedPrefRepo
import com.mocklets.pluto.modules.preferences.getSharePreferencesFiles
import com.mocklets.pluto.modules.preferences.ui.SharedPrefAdapter
import com.mocklets.pluto.modules.preferences.ui.SharedPrefFile
import com.mocklets.pluto.modules.preferences.ui.SharedPrefViewModel

internal class SharedPrefFilterFragment : Fragment(R.layout.pluto___fragment_shared_pref_filter) {

    private val binding by viewBinding(PlutoFragmentSharedPrefFilterBinding::bind)
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
            requireContext().toast(requireContext().getString(R.string.pluto___preferences_cleared))
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
                holder?.absoluteAdapterPosition?.let { prefAdapter.notifyItemChanged(it) }
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
