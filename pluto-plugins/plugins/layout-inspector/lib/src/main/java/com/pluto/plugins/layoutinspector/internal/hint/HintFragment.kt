package com.pluto.plugins.layoutinspector.internal.hint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiHintFragmentBinding
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.viewBinding

internal class HintFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoLiHintFragmentBinding::bind)
    private val settingsAdapter: BaseAdapter by autoClearInitializer { HintAdapter() }
    private val viewModel: HintViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_li___hint_fragment, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = settingsAdapter
            addItemDecoration(CustomItemDecorator(context, 16f.dp.toInt()))
        }
        viewModel.list.removeObserver(settingsObserver)
        viewModel.list.observe(viewLifecycleOwner, settingsObserver)
    }

    private val settingsObserver = Observer<List<HintItem>> {
        settingsAdapter.list = it
    }
}
