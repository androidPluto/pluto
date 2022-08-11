package com.pluto.plugin.utilities.views

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoViewKeyValuePairBinding
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan

class KeyValuePairView(context: Context) : ConstraintLayout(context) {

    private val binding = PlutoViewKeyValuePairBinding.inflate(LayoutInflater.from(context), this, true)

    fun set(data: KeyValuePairData) {
        binding.key.text = data.key
        binding.value.setSpan {
            data.value?.let {
                append(it)
            } ?: run {
                append(italic(fontColor("~ null", context.color(R.color.pluto___text_dark_40))))
            }
        }
        if (data.showClickIndicator) {
            binding.value.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pluto___ic_chevron_right, 0)
        }
        data.onClick?.let {
            binding.root.isClickable = true
            binding.root.setOnDebounceClickListener { data.onClick.invoke() }
        } ?: run {
            binding.root.isClickable = false
        }
    }
}
