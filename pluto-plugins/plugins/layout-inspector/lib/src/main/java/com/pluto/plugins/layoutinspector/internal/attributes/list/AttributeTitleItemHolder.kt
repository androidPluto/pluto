package com.pluto.plugins.layoutinspector.internal.attributes.list

import android.view.ViewGroup
import androidx.annotation.Keep
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiItemViewAttrTitleBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class AttributeTitleItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_li___item_view_attr_title), actionListener) {

    private val binding = PlutoLiItemViewAttrTitleBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is AttributeTitle) {
            binding.title.text = item.title
        }
    }
}

@Keep
data class AttributeTitle(val title: String?) : ListItem()
