package com.pluto.plugins.layoutinspector.internal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiParamsPreviewPanelBinding
import com.pluto.plugins.layoutinspector.internal.inspect.getIdString
import com.pluto.utilities.extensions.color
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class ParamsPreviewPanel : ConstraintLayout {

    private val binding = PlutoLiParamsPreviewPanelBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun refresh(view: View, onViewAttrRequested: () -> Unit, onViewHierarchyRequested: () -> Unit, onCloseRequested: () -> Unit) {
        binding.viewId.setSpan {
            view.getIdString()?.let {
                append(it)
            } ?: run {
                append(regular(italic(fontColor("NO_ID", context.color(R.color.pluto___text_dark_40)))))
            }
        }
        binding.viewType.text = if (view is ViewGroup) "viewGroup" else "view"
        binding.viewClass.text = view.javaClass.canonicalName

        binding.viewAttrCta.setOnDebounceClickListener {
            onViewAttrRequested.invoke()
        }
        binding.viewHierarchyCta.setOnDebounceClickListener {
            onViewHierarchyRequested.invoke()
        }
        binding.close.setOnDebounceClickListener {
            onCloseRequested.invoke()
        }
    }
}
