package com.pluto.plugins.network.internal.bandwidth.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import okhttp3.internal.toLongOrDefault

@BindingAdapter("android:text")
fun setText(view: TextView, text: Long?) {
    val oldText = view.text
    if (text.toString() === oldText) {
        return
    }
    if (!haveContentsChanged(text.toString(), oldText)) {
        return // No content changes, so don't set anything.
    }
    view.text = text.toString()
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getText(view: TextView): Long {
    return view.text.toString().toLongOrDefault(0)
}

private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {
    if (str1 == null != (str2 == null)) {
        return true
    } else if (str1 == null) {
        return false
    }
    val length = str1.length
    if (length != str2!!.length) {
        return true
    }
    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}
