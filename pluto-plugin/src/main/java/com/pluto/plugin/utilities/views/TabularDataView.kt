package com.pluto.plugin.utilities.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugin.databinding.PlutoViewTabularDataBinding

class TabularDataView : ConstraintLayout {

    private val binding = PlutoViewTabularDataBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(title: String, keyValuePairs: List<KeyValuePairData>) {
        binding.tabularDataTitle.text = title
        keyValuePairs.forEach { binding.tabularDataContainer.addView(KeyValuePairView(context).apply { set(it) }) }
    }
}
