package com.pluto.utilities.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.utilities.databinding.PlutoViewTabularDataBinding
import com.pluto.utilities.views.keyvalue.KeyValuePairData
import com.pluto.utilities.views.keyvalue.KeyValuePairView

class TabularDataView : ConstraintLayout {

    private val binding = PlutoViewTabularDataBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(title: String, keyValuePairs: List<KeyValuePairData> = arrayListOf()) {
        binding.tabularDataTitle.text = title
        binding.tabularDataContainer.removeAllViews()
        keyValuePairs.forEach { binding.tabularDataContainer.addView(KeyValuePairView(context).apply { set(it) }) }
    }
}
