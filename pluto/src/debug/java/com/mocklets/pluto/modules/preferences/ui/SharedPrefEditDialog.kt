package com.mocklets.pluto.modules.preferences.ui

import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DeviceInfo
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.extensions.showKeyboard
import com.mocklets.pluto.core.sharing.Shareable
import com.mocklets.pluto.core.sharing.lazyContentSharer
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoLayoutSharedPrefEditBinding
import java.util.*

internal class SharedPrefEditDialog(
    private val fragment: Fragment,
    private val onSave: (SharedPrefKeyValuePair, Any) -> Unit
) : BottomSheetDialog(fragment.requireContext(), R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto___layout_shared_pref_edit)
    private val binding = PlutoLayoutSharedPrefEditBinding.bind(sheetView)
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
                            fragment.lifecycleScope.launchWhenResumed {
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
            binding.value.addTextChangedListener {
                val isValid = it.toString().isValid(pref.value)
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
