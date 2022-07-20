package com.pluto.plugin.utilities.views

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoViewKeyValuePairBinding
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan

class KeyValuePairView(context: Context) : ConstraintLayout(context) {

    private val binding = PlutoViewKeyValuePairBinding.inflate(LayoutInflater.from(context), this, true)

    fun set(key: String, value: CharSequence?, onClick: (() -> Unit)? = null) {
        binding.key.text = key
        binding.value.setSpan {
            value?.let {
                append(it)
            } ?: run {
                append(italic(fontColor("~ null", context.color(R.color.pluto___text_dark_40))))
            }
        }
        onClick?.let {
            binding.root.isClickable = true
            binding.root.setDebounceClickListener { onClick.invoke() }
        } ?: run {
            binding.root.isClickable = false
        }
    }
}
