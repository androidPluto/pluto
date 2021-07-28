package com.mocklets.pluto.modules.exceptions.ui

import android.view.ViewGroup
import com.mocklets.pluto.core.DeviceFingerPrint
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.exceptions.ExceptionData
import com.mocklets.pluto.modules.exceptions.ProcessThread
import com.mocklets.pluto.modules.exceptions.ThreadData
import com.mocklets.pluto.modules.exceptions.ThreadStates
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemDetailsDeviceHolder
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemDetailsHeaderHolder
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemDetailsThreadHolder
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemDetailsThreadStatesHolder
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemDetailsThreadStatesItemHolder
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemHolder

internal class CrashesAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ExceptionEntity -> ITEM_TYPE_CRASH
            is ExceptionData -> ITEM_DETAILS_TYPE_HEADER
            is ThreadData -> ITEM_DETAILS_TYPE_THREAD
            is DeviceFingerPrint -> ITEM_DETAILS_TYPE_DEVICE
            is ThreadStates -> ITEM_DETAILS_TYPE_THREAD_STATES
            is ProcessThread -> ITEM_DETAILS_TYPE_THREAD_STATES_ITEM
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_CRASH -> CrashItemHolder(parent, listener)
            ITEM_DETAILS_TYPE_HEADER -> CrashItemDetailsHeaderHolder(parent, listener)
            ITEM_DETAILS_TYPE_THREAD -> CrashItemDetailsThreadHolder(parent, listener)
            ITEM_DETAILS_TYPE_DEVICE -> CrashItemDetailsDeviceHolder(parent, listener)
            ITEM_DETAILS_TYPE_THREAD_STATES -> CrashItemDetailsThreadStatesHolder(parent, listener)
            ITEM_DETAILS_TYPE_THREAD_STATES_ITEM -> CrashItemDetailsThreadStatesItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_CRASH = 1000
        const val ITEM_DETAILS_TYPE_HEADER = 1100
        const val ITEM_DETAILS_TYPE_THREAD = 1101
        const val ITEM_DETAILS_TYPE_DEVICE = 1102
        const val ITEM_DETAILS_TYPE_THREAD_STATES = 1103
        const val ITEM_DETAILS_TYPE_THREAD_STATES_ITEM = 1104
    }
}
