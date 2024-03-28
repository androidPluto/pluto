package com.pluto.utilities.selector.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoSelectorDialogBinding
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorData
import com.pluto.utilities.selector.SelectorOption
import com.pluto.utilities.selector.lazySelector
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

class SelectorDialog : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoSelectorDialogBinding::bind)
    private val selector by lazySelector()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___selector_dialog, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        selector.singleChoiceData.observe(viewLifecycleOwner) {
            setupSingleChoiceUI(it)
        }
        selector.multiChoiceData.observe(viewLifecycleOwner) {
            setupMultiChoiceUI(it)
        }
    }

    private fun setupSingleChoiceUI(data: SelectorData<SelectorOption>) {
        binding.doneCta.visibility = GONE
        binding.clear.visibility = GONE
        binding.list.apply {
            adapter = SingleSelectorAdapter(
                object : DiffAwareAdapter.OnActionListener {
                    override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
                        if (data is SelectorOption) {
                            selector.singleChoiceResult.postValue(data)
                            dismiss()
                        }
                    }
                },
                data.preSelected
            ).apply { this.list = data.list }
            addItemDecoration(CustomItemDecorator(requireContext(), DECORATOR_DIVIDER_PADDING))
        }
    }

    private fun setupMultiChoiceUI(data: SelectorData<List<SelectorOption>>) {
        val tempSelectedOptionLiveData = SingleLiveEvent<List<SelectorOption>>()
        data.preSelected.let { tempSelectedOptionLiveData.postValue(it) }

        binding.list.apply {
            adapter = MultiSelectorAdapter(
                object : DiffAwareAdapter.OnActionListener {
                    override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
                        if (data is SelectorOption) {
                            val tempSet = tempSelectedOptionLiveData.value?.toHashSet() ?: hashSetOf()
                            if (!tempSet.add(data)) {
                                tempSet.remove(data)
                            }
                            tempSelectedOptionLiveData.postValue(tempSet.toList())
                        }
                    }
                },
                tempSelectedOptionLiveData
            ).apply { this.list = data.list }
            addItemDecoration(CustomItemDecorator(requireContext(), DECORATOR_DIVIDER_PADDING))
        }
        binding.doneCta.setOnDebounceClickListener {
            selector.multiChoiceResult.postValue(tempSelectedOptionLiveData.value?.toList() ?: emptyList())
            dismiss()
        }

        binding.clear.setOnDebounceClickListener {
            selector.multiChoiceResult.postValue(emptyList())
            dismiss()
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
