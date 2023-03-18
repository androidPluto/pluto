package com.pluto.utilities.views.keyvalue.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoFragmentKeyValuePairEditBinding
import com.pluto.utilities.extensions.color
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest
import com.pluto.utilities.views.keyvalue.KeyValuePairEditResult

class KeyValuePairEditDialog : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentKeyValuePairEditBinding::bind)
    private val keyValuePairEditor: KeyValuePairEditViewModel by lazyKeyValuePairEditor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_key_value_pair_edit, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnDebounceClickListener {
            dismiss()
        }

        arguments?.getParcelable<KeyValuePairEditRequest>("data")?.let { data ->
            binding.editKeyDescription.setSpan {
                append(getString(R.string.pluto___key_value_editor_description))
                append(" ")
                append(bold(fontColor(data.key, context.color(R.color.pluto___text_dark_80))))
            }
            binding.editValue.requestFocus()
            binding.editValue.setText(data.value)
            binding.editValue.setSelection(data.value.toString().length)
            binding.editValue.isFocusableInTouchMode = true
            binding.editValue.isFocusable = true
            binding.editValue.hint = data.hint
            data.inputType.type?.let {
                binding.editValue.inputType = it
            }

            data.candidateOptions?.let { list ->
                binding.candidateOptionGroup.visibility = VISIBLE
                list.forEach {
                    binding.candidateOptions.addView(Chip(context).apply {
                        text = it
                        setTextAppearance(R.style.PlutoChipTextStyle)
                        textStartPadding = CHIP_PADDING
                        textEndPadding = CHIP_PADDING
                        setOnDebounceClickListener { _ ->
                            keyValuePairEditor.saveResult(KeyValuePairEditResult(data.key, it))
                            dismiss()
                        }
                    })
                }
            }

            binding.saveCta.setOnDebounceClickListener {
                keyValuePairEditor.saveResult(KeyValuePairEditResult(data.key, binding.editValue.text.toString()))
                dismiss()
            }
        }
    }

    companion object {
        private const val CHIP_PADDING = 16f
    }
}