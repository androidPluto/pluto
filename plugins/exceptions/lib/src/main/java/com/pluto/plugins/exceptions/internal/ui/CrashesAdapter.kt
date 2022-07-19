package com.pluto.plugins.exceptions.internal.ui

import android.view.ViewGroup
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugins.exceptions.internal.DeviceInfo
import com.pluto.plugins.exceptions.internal.ExceptionData
import com.pluto.plugins.exceptions.internal.ThreadData
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity
import com.pluto.plugins.exceptions.internal.ui.holder.CrashItemDetailsDeviceHolder
import com.pluto.plugins.exceptions.internal.ui.holder.CrashItemDetailsHeaderHolder
import com.pluto.plugins.exceptions.internal.ui.holder.CrashItemDetailsThreadHolder
import com.pluto.plugins.exceptions.internal.ui.holder.CrashItemHolder

internal class CrashesAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ExceptionEntity -> ITEM_TYPE_CRASH
            is ExceptionData -> ITEM_DETAILS_TYPE_HEADER
            is ThreadData -> ITEM_DETAILS_TYPE_THREAD
            is DeviceInfo -> ITEM_DETAILS_TYPE_DEVICE
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_CRASH -> CrashItemHolder(parent, listener)
            ITEM_DETAILS_TYPE_HEADER -> CrashItemDetailsHeaderHolder(parent, listener)
            ITEM_DETAILS_TYPE_THREAD -> CrashItemDetailsThreadHolder(parent, listener)
            ITEM_DETAILS_TYPE_DEVICE -> CrashItemDetailsDeviceHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_CRASH = 1000
        const val ITEM_DETAILS_TYPE_HEADER = 1100
        const val ITEM_DETAILS_TYPE_THREAD = 1101
        const val ITEM_DETAILS_TYPE_DEVICE = 1102
    }
}
