package com.pluto.plugins.preferences.ui

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.preferences.R
import com.pluto.plugins.preferences.SharedPrefRepo
import com.pluto.plugins.preferences.databinding.PlutoPrefFragmentEditBinding
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.delayedLaunchWhenResumed
import com.pluto.utilities.extensions.showKeyboard
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.share.Shareable
import com.pluto.utilities.share.lazyContentSharer
import com.pluto.utilities.viewBinding

class EditFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoPrefFragmentEditBinding::bind)
    private val viewModel: SharedPrefViewModel by activityViewModels()
    private val contentSharer by lazyContentSharer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_pref___fragment_edit, container, false)

    override fun getTheme(): Int = R.style.PlutoPrefBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

                dialog.behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            viewLifecycleOwner.lifecycleScope.delayedLaunchWhenResumed(KEYBOARD_DELAY) {
                                binding.value.requestFocus()
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }
                })
            }
        }

        binding.value.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            }
        }
        viewModel.current.removeObserver(detailsObserver)
        viewModel.current.observe(viewLifecycleOwner, detailsObserver)
    }

    private val detailsObserver = Observer<SharedPrefKeyValuePair> {
        updateUi(it)
    }

    private fun updateUi(pref: SharedPrefKeyValuePair) {
        if (isPrefTypeSupported(pref.value)) {
            binding.value.isFocusableInTouchMode = true
            binding.value.isFocusable = true
            binding.save.visibility = View.VISIBLE

            binding.value.doOnTextChanged { text, _, _, _ ->
                val isValid = text.toString().isValid(pref.value)
                binding.save.isEnabled = isValid
                binding.save.setBackgroundColor(
                    requireContext().color(if (isValid) com.pluto.plugin.R.color.pluto___dark else com.pluto.plugin.R.color.pluto___dark_40)
                )
            }

            binding.value.inputType = handleKeypad(pref.value)
            binding.value.hint = setHint(pref.value)
            binding.save.visibility = View.VISIBLE
            binding.disabled.visibility = View.GONE
        } else {
            binding.value.isFocusableInTouchMode = false
            binding.value.isFocusable = false
            binding.save.visibility = View.GONE
            binding.disabled.visibility = View.VISIBLE
        }
        binding.file.text = pref.prefLabel
        binding.key.text = pref.key
        binding.value.setText(pref.value.toString())
        binding.value.setSelection(pref.value.toString().length)
        binding.save.setOnDebounceClickListener {
            SharedPrefRepo.set(requireContext(), pref, binding.value.text.toString().convert(pref.value))
            viewModel.refresh()
            dismiss()
        }
        binding.cta.setOnDebounceClickListener {
            contentSharer.share(
                Shareable(
                    content = "${pref.key} : ${pref.value}",
                    title = "Share Shared Preference",
                    fileName = "Preference data from Pluto"
                )
            )
        }
    }

    private fun setHint(value: Any?): CharSequence = when (value) {
        is Int,
        is Long -> "12345"
        is Float -> "1234.89"
        is Boolean -> "true"
        else -> "abcde 123"
    }

    private fun isPrefTypeSupported(value: Any?): Boolean = value !is Set<*>

    private fun handleKeypad(value: Any?): Int = when (value) {
        is Int,
        is Long -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        is Float -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
        else -> InputType.TYPE_CLASS_TEXT
    }

    companion object {
        const val KEYBOARD_DELAY = 200L
    }
}

private fun String.convert(value: Any?): Any = when (value) {
    is Int -> this.toInt()
    is Long -> this.toLong()
    is Float -> this.toFloat()
    is Boolean -> this.toBoolean()
    else -> this
}

private fun String.isValid(value: Any?): Boolean = when (value) {
    is Boolean -> this.lowercase() == "true" || this.lowercase() == "false"
    is Int,
    is Float,
    is Long -> this.isNotEmpty()
    else -> true
}
