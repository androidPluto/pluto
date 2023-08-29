package com.pluto.plugins.network.base.internal.share.holders

import android.view.ViewGroup
import com.pluto.plugins.network.base.R
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ShareHeadingHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_share_option_header), actionListener) {
    override fun onBind(item: ListItem) {
    }
}
