package com.pluto.plugins.layoutinspector.internal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.layoutinspector.databinding.PlutoLiParamsPreviewPanelBinding
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ParamsPreviewPanel : ConstraintLayout {

    private val binding = PlutoLiParamsPreviewPanelBinding.inflate(LayoutInflater.from(context), this, true)
//    private val pluginAdapter: BaseAdapter by lazy { ControlCtaAdapter(onActionListener) }
    private var mListener: OnClickListener? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun refresh(view: View, listener: OnClickListener? = null) {
        binding.details.text = "${view.width}, ${view.height} : ${view.javaClass.simpleName}"
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {

        }
    }

    interface OnClickListener {
        fun onClick(id: String)
    }
}
