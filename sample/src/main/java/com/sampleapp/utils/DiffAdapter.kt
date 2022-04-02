package com.sampleapp.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DiffAdapter<VH : DiffAwareHolder> : RecyclerView.Adapter<DiffAwareHolder>() {

    private val asyncListDiffer by lazy {
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<ListItem>() {
                override fun areItemsTheSame(oldItem: ListItem, item: ListItem): Boolean = oldItem.isSame(item)

                override fun areContentsTheSame(oldItem: ListItem, item: ListItem): Boolean = oldItem.isEqual(item)

                override fun getChangePayload(oldItem: ListItem, item: ListItem): Any? = oldItem.getPayload(item)
            }
        )
    }

    var list: List<ListItem>
        set(value) = asyncListDiffer.submitList(value)
        get() = asyncListDiffer.currentList

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DiffAwareHolder, position: Int) {
        holder.update(list[position])
        holder.onBind(list[position])
    }

    override fun onBindViewHolder(holder: DiffAwareHolder, position: Int, payloads: MutableList<Any>) {
        holder.update(list[position])
        if (payloads.isNotEmpty()) {
            holder.onPayloadChanged(list[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onViewRecycled(holder: DiffAwareHolder) {
        super.onViewRecycled(holder)
        holder.onRecycle()
    }

    override fun onViewAttachedToWindow(holder: DiffAwareHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttachViewHolder()
    }

    override fun onViewDetachedFromWindow(holder: DiffAwareHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetachViewHolder()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    interface OnActionListener {
        fun onAction(action: String, data: ListItem, holder: DiffAwareHolder? = null)
    }
}

open class ListItem {
    open fun isSame(other: Any): Boolean = this == other
    open fun isEqual(other: Any): Boolean = this == other
    open fun getPayload(other: ListItem): Any? = null
}
