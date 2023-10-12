package com.pluto.ui

import com.pluto.utilities.list.ListItem

internal class ListWrapper<T>(private val data: T) : ListItem() {
    fun get(): T = data
}
