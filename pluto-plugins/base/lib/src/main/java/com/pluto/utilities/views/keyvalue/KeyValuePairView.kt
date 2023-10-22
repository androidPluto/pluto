package com.pluto.utilities.views.keyvalue

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoViewKeyValuePairBinding
import com.pluto.utilities.extensions.color
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

class KeyValuePairView : ConstraintLayout {

    private val binding = PlutoViewKeyValuePairBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(data: KeyValuePairData) {
        binding.key.text = data.key
        binding.value.setSpan {
            data.value?.let {
                append(it)
            } ?: run {
                append(italic(fontColor("~ null ~", context.color(R.color.pluto___text_dark_40))))
            }
        }

        binding.value.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (data.showClickIndicator) R.drawable.pluto___ic_chevron_right else 0, 0)
        data.onClick?.let {
            binding.root.isClickable = true
            binding.root.setOnDebounceClickListener { data.onClick.invoke() }
        } ?: run {
            binding.root.isClickable = false
        }
    }
}
