package com.pluto.preferences.ui

import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pluto.plugin.utilities.DeviceInfo
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.extensions.showKeyboard
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.preferences.R
import com.pluto.preferences.databinding.PlutoPrefLayoutSharedPrefEditBinding
import java.util.Locale

internal class EditDialog(
    private val fragment: Fragment,
    private val onSave: (SharedPrefKeyValuePair, Any) -> Unit
) : BottomSheetDialog(fragment.requireContext(), R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto_pref___layout_shared_pref_edit)
    private val binding = PlutoPrefLayoutSharedPrefEditBinding.bind(sheetView)
    private val deviceInfo = DeviceInfo(context)
    private val contentSharer by fragment.lazyContentSharer()

    init {
        setContentView(sheetView)
        (sheetView.parent as View).background =
            ColorDrawable(ContextCompat.getColor(context, R.color.pluto___transparent))

        setOnShowListener { dialog ->
            if (dialog is BottomSheetDialog) {
                val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            fragment.viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                                binding.value.requestFocus()
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }
                })
                behavior.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    isHideable = false
                    skipCollapsed = true
                    peekHeight = deviceInfo.height
                }
            }
        }

        binding.value.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            }
        }
    }

    fun show(pref: SharedPrefKeyValuePair) {
        if (isPrefTypeSupported(pref.value)) {
            binding.value.isFocusableInTouchMode = true
            binding.value.isFocusable = true
            binding.save.visibility = VISIBLE

            binding.value.doOnTextChanged { text, _, _, _ ->
                val isValid = text.toString().isValid(pref.value)
                binding.save.isEnabled = isValid
                binding.save.setBackgroundColor(context.color(if (isValid) R.color.pluto___dark else R.color.pluto___dark_40))
            }

            binding.value.inputType = handleKeypad(pref.value)
            binding.value.hint = setHint(pref.value)
            binding.save.visibility = VISIBLE
            binding.disabled.visibility = GONE
        } else {
            binding.value.isFocusableInTouchMode = false
            binding.value.isFocusable = false
            binding.save.visibility = GONE
            binding.disabled.visibility = VISIBLE
        }
        binding.file.text = pref.prefLabel
        binding.key.text = pref.key
        binding.value.setText(pref.value.toString())
        binding.value.setSelection(pref.value.toString().length)
        binding.save.setDebounceClickListener {
            onSave.invoke(pref, binding.value.text.toString().convert(pref.value))
        }
        binding.cta.setDebounceClickListener {
            contentSharer.share(
                Shareable(
                    content = "${pref.key} : ${pref.value}",
                    title = "Share Shared Preference",
                    fileName = "Preference data from Pluto"
                )
            )
        }
        show()
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
}

private fun String.convert(value: Any?): Any = when (value) {
    is Int -> this.toInt()
    is Long -> this.toLong()
    is Float -> this.toFloat()
    is Boolean -> this.toBoolean()
    else -> this
}

private fun String.isValid(value: Any?): Boolean = when (value) {
    is Boolean -> this.lowercase(Locale.getDefault()) == "true" || this.lowercase(Locale.getDefault()) == "false"
    is Int,
    is Float,
    is Long -> this.isNotEmpty()
    else -> true
}
