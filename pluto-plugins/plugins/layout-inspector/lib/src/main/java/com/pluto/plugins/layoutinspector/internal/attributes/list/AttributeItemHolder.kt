package com.pluto.plugins.layoutinspector.internal.attributes.list

import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiItemViewAttrBinding
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.type.MutableAttributeTag
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.views.keyvalue.KeyValuePairData

internal class AttributeItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_li___item_view_attr), actionListener) {

    private val binding = PlutoLiItemViewAttrBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is Attribute<*>) {
            binding.content.set(
                KeyValuePairData(
                    key = item.type.title,
                    value = item.valueString,
                    showClickIndicator = item.type.tag != MutableAttributeTag.Immutable,
                    onClick = getAction(item)
                )
            )
        }
    }

    private fun getAction(item: Attribute<*>): (() -> Unit)? = if (item.type.tag != MutableAttributeTag.Immutable) {
        { onAction("click") }
    } else {
        null
    }
}
