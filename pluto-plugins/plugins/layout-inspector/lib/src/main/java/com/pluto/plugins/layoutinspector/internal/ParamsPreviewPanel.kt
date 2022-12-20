package com.pluto.plugins.layoutinspector.internal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.layoutinspector.databinding.PlutoLiParamsPreviewPanelBinding
import com.pluto.plugins.layoutinspector.internal.ViewUtils.getIdString
import com.pluto.utilities.setOnDebounceClickListener

internal class ParamsPreviewPanel : ConstraintLayout {

    private val binding = PlutoLiParamsPreviewPanelBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun refresh(view: View, listener: (String) -> Unit) {
        binding.viewId.text = view.getIdString()
        binding.viewType.text = if (view is ViewGroup) "viewGroup" else "view"
        binding.viewClass.text = view.javaClass.canonicalName
        binding.viewDimens.text = "${view.width} x ${view.height} dp"

        binding.viewAttrCta.setOnDebounceClickListener {
            listener.invoke("view_attr")
        }
        binding.viewHierarchyCta.setOnDebounceClickListener {
            listener.invoke("view_hierarchy")
        }
    }
}
