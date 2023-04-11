package com.pluto.plugins.layoutinspector.internal.hierarchy.list

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiItemViewHierarchyBinding
import com.pluto.plugins.layoutinspector.internal.hierarchy.Hierarchy
import com.pluto.plugins.layoutinspector.internal.inspect.getIdString
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class HierarchyItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_li___item_view_hierarchy), actionListener) {

    private val binding = PlutoLiItemViewHierarchyBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is Hierarchy) {
            binding.viewTitle.text = item.view.javaClass.simpleName
            binding.viewSubtitle.setSpan {
                item.view.getIdString()?.let {
                    append(it)
                } ?: run {
                    append(regular(italic(fontColor("NO_ID", context.color(R.color.pluto___text_dark_40)))))
                }
                append(" {(${item.view.left},${item.view.top}),(${item.view.right},${item.view.bottom})}")
            }
            binding.viewAttrCta.setOnDebounceClickListener {
                onAction(ACTION_ATTRIBUTE)
            }
            binding.expandStateIndicator.setImageResource(
                if (item.isExpanded) R.drawable.pluto_li___ic_hierarchy_show_less
                else R.drawable.pluto_li___ic_hierarchy_show_more
            )
            binding.expandStateIndicator.visibility = if (item.view is ViewGroup) VISIBLE else GONE
            if (item.view is ViewGroup) {
                binding.contentWrapper.setOnDebounceClickListener { onAction(ACTION_EXPAND_COLLAPSE) }
            } else {
                binding.contentWrapper.setOnDebounceClickListener(action = null)
            }
            val layoutParams: ConstraintLayout.LayoutParams = binding.viewTitle.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginStart = if (item.view !is ViewGroup) 8f.dp.toInt() else 0
            binding.viewTitle.layoutParams = layoutParams
            binding.root.setLayerCount(item.layerCount, item.sysLayerCount);
        }
    }

    companion object {
        const val ACTION_ATTRIBUTE = "attribute"
        const val ACTION_EXPAND_COLLAPSE = "expand_collapse"
    }
}
