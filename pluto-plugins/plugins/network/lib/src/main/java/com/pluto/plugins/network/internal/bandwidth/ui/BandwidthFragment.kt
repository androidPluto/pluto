package com.pluto.plugins.network.internal.bandwidth.ui

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.network.PlutoNetwork
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentBandwidthBinding
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.settings.SettingsPreferences

class BandwidthFragment : BottomSheetDialogFragment() {

    private lateinit var binding: PlutoNetworkFragmentBandwidthBinding

    private val viewModel: BandwidthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = PlutoNetworkFragmentBandwidthBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        setInfinityTextWatcher(binding.valueDownloadLimit, binding.valueUploadLimit)
        return binding.root
    }

    private fun setInfinityTextWatcher(vararg editTexts: EditText) {
        for (editText in editTexts) {
            InfinityTextWatcher(editText, Long.MAX_VALUE)
        }
    }

    override fun getTheme(): Int = R.style.PlutoNetworkBottomSheetDialog

    private fun enableInputFields(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    private fun disableInputFields(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_NULL
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.dnsResolutionTimeout.observe(viewLifecycleOwner) {
            updateSaveCtaState()
        }
        viewModel.downloadSpeedInMbps.observe(viewLifecycleOwner) {
            updateSaveCtaState()
        }
        viewModel.uploadSpeedInMbps.observe(viewLifecycleOwner) {
            updateSaveCtaState()
        }
        viewModel.isBandwidthLimitEnabled.observe(viewLifecycleOwner) {
            if (it) {
                enableInputFields(binding.valueDnsDelay, binding.valueDownloadLimit, binding.valueDownloadLimit)
            } else {
                disableInputFields(binding.valueDnsDelay, binding.valueDownloadLimit, binding.valueDownloadLimit)
                viewModel.reset()
            }
            updateSaveCtaState()
        }
        binding.save.setOnDebounceClickListener {
            // saving values
            SettingsPreferences.isBandwidthLimitEnabled = viewModel.isBandwidthLimitEnabled.value!!
            SettingsPreferences.bandWidthLimitDownloadMbps = viewModel.downloadSpeedInMbps.value!!
            SettingsPreferences.bandWidthDnsResolutionDelay = viewModel.dnsResolutionTimeout.value!!
            SettingsPreferences.bandWidthLimitUploadMbps = viewModel.uploadSpeedInMbps.value!!
            // updating limits
            PlutoNetwork.updateBandwidthLimitValues()
            dismiss()
        }
        binding.cta.setOnDebounceClickListener {
            dismiss()
        }
    }

    private fun updateSaveCtaState() {
        binding.save.isVisible = viewModel.areValuesChanged()
        binding.disabled.isVisible = !viewModel.areValuesChanged()
    }
}
