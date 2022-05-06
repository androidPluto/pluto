package com.pluto.plugin.utilities.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.ListItem
import java.lang.IllegalStateException

fun RecyclerView.linearLayoutManager(): LinearLayoutManager? {
    if (layoutManager is LinearLayoutManager) {
        return layoutManager as LinearLayoutManager
    }
    return null
}

fun RecyclerView.setList(list: List<ListItem>) {
    adapter?.let {
        if (it is BaseAdapter) {
            it.list = list
        } else {
            throw IllegalStateException("adapter is not BaseAdapter")
        }
    } ?: run {
        throw IllegalStateException("adapter not set")
    }
}
