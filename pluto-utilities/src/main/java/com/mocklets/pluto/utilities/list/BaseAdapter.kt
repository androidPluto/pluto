package com.mocklets.pluto.utilities.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal abstract class BaseAdapter : DiffAwareAdapter<DiffAwareHolder>() {
    abstract fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder?
    abstract fun getItemViewType(item: ListItem): Int?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiffAwareHolder {
        onViewHolderCreated(parent, viewType)?.let {
            return it
        }
        throw IllegalArgumentException("BaseAdapter: unknown viewType $viewType")
    }

    override fun getItemViewType(position: Int): Int {
        getItemViewType(list[position])?.let {
            return it
        }
        throw IllegalArgumentException("BaseAdapter: type not defined for ${list[position]}")
    }
}

internal abstract class DiffAwareHolder(
    view: View,
    private val listener: DiffAwareAdapter.OnActionListener?
) : RecyclerView.ViewHolder(view) {
    val context: Context = view.context
    var item: ListItem? = null

    abstract fun onBind(item: ListItem)

    open fun onPayloadChanged(item: ListItem, payloads: MutableList<Any>) {}

    open fun onRecycle() {}

    open fun onAttachViewHolder() {}

    open fun onDetachViewHolder() {}

    fun onAction(type: String) {
        listener?.onAction(type, item!!, this)
    }

    fun update(item: ListItem) {
        this.item = item
    }
}
