package com.pluto.plugins.utilities.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.linearLayoutManager(): LinearLayoutManager? {
    if (layoutManager is LinearLayoutManager) {
        return layoutManager as LinearLayoutManager
    }
    return null
}
