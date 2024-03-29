package com.pluto.plugins.layoutinspector.internal.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiControlsWidgetBinding
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ControlsWidget : ConstraintLayout {

    private val binding = PlutoLiControlsWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    private val pluginAdapter: BaseAdapter by lazy { ControlCtaAdapter(onActionListener) }
    private var mListener: OnClickListener? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun initialise(ctas: List<ControlCta>, listener: OnClickListener? = null) {
        mListener = listener
        binding.list.apply {
            adapter = pluginAdapter
            addItemDecoration(
                DividerItemDecoration(context, LinearLayout.HORIZONTAL).apply {
                    setDrawable(ContextCompat.getDrawable(context, R.drawable.pluto_li___item_divider)!!)
                }
            )
        }
        pluginAdapter.list = ctas
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is ControlCta) {
                mListener?.onClick(data.id)
            }
        }
    }

    interface OnClickListener {
        fun onClick(id: String)
    }
}
