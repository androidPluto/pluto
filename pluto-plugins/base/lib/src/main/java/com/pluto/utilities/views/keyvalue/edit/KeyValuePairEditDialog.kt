package com.pluto.utilities.views.keyvalue.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoFragmentKeyValuePairEditBinding
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest
import com.pluto.utilities.views.keyvalue.KeyValuePairEditResult

class KeyValuePairEditDialog : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentKeyValuePairEditBinding::bind)
    private val keyValuePairEditor: KeyValuePairEditor by lazyKeyValuePairEditor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_key_value_pair_edit, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                keyValuePairEditor.data.value?.let { data ->
                    handleUI(data)
                }
            }
        }

        binding.close.setOnDebounceClickListener {
            dismiss()
        }
    }

    private fun handleUI(data: KeyValuePairEditRequest) {
        binding.editKeyDescription.setSpan {
            append(getString(R.string.pluto___key_value_editor_description))
            append(" ")
            append(bold(fontColor(data.key, context.color(R.color.pluto___text_dark_80))))
        }
        if (data.shouldAllowFreeEdit) {
            handleInputType(data)
        } else {
            binding.editGroup.visibility = GONE
        }
        handleCandidateOptions(data)
        binding.saveCta.setOnDebounceClickListener {
            saveResult(data.key, binding.editValue.text.toString())
        }
    }

    private fun handleCandidateOptions(data: KeyValuePairEditRequest) {
        data.processedCandidateOptions?.let { list ->
            binding.candidateOptionDescription.visibility = if (data.shouldAllowFreeEdit) VISIBLE else GONE
            binding.candidateOptions.visibility = VISIBLE
            list.forEach {
                binding.candidateOptions.addView(
                    Chip(context).apply {
                        text = it.trim()
                        setTextAppearance(R.style.PlutoChipTextStyle)
                        if (it == data.value) {
                            chipIcon = ContextCompat.getDrawable(context, R.drawable.pluto___ic_edit_option_selected)
                            chipIconSize = 18f.dp
                            iconStartPadding = CHIP_ICON_PADDING
                            textStartPadding = CHIP_TEXT_PADDING
                        }
                        setOnDebounceClickListener { _ ->
                            saveResult(data.key, it)
                        }
                    }
                )
            }
        }
    }

    private fun handleInputType(data: KeyValuePairEditRequest) {
        binding.editGroup.visibility = VISIBLE
        binding.editValue.setText(data.value)
        binding.editValue.hint = data.hint
        binding.editValue.requestFocus()
        binding.editValue.isFocusableInTouchMode = true
        binding.editValue.isFocusable = true
        binding.editValue.post {
            binding.editValue.setSelection(data.value?.length ?: 0)
        }
        data.inputType.type?.let {
            binding.editValue.inputType = it
        }
        binding.editValue.doOnTextChanged { text, _, _, _ ->
            val isValid = data.isValidValue(text?.toString())
            binding.saveCta.isEnabled = isValid
            binding.saveCtaIcon.setBackgroundColor(
                requireContext().color(if (isValid) R.color.pluto___emerald else R.color.pluto___disabled_cta)
            )
        }
        binding.editValue.setOnEditorActionListener { _, actionId, _ ->
            if (data.isValidValue(binding.editValue.text?.toString()) && actionId == EditorInfo.IME_ACTION_DONE) {
                saveResult(data.key, binding.editValue.text.toString())
                true
            } else false
        }
    }

    private fun saveResult(key: String, value: String) {
        keyValuePairEditor.data.value?.let { data ->
            keyValuePairEditor.saveResult(KeyValuePairEditResult(key, value, data.metaData))
        }
        dismiss()
    }

    companion object {
        private const val CHIP_TEXT_PADDING = 8f
        private const val CHIP_ICON_PADDING = 10f
    }
}
