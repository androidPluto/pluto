package com.pluto.plugins.layoutinspector.internal.hierarchy.list

import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiItemViewHierarchyBinding
import com.pluto.plugins.layoutinspector.internal.hierarchy.Hierarchy
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class HierarchyItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_li___item_view_hierarchy), actionListener) {

    private val binding = PlutoLiItemViewHierarchyBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is Hierarchy) {
            binding.name.text = item.view.javaClass.simpleName + "--" + item.view.javaClass.simpleName
        }
    }
}
