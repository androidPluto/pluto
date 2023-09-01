package com.pluto.plugins.network.internal.bandwidth.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class InfinityTextWatcher(private val editText: EditText, private val threshold: Long) :
    TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        val inputText = editable.toString()
        try {
            val value = inputText.toLong()
            if (value > threshold) {
                editText.setText(INFINITY)
            }
        } catch (e: NumberFormatException) {
            // Handle parsing errors or non-numeric input
        }
    }
    companion object {
        const val INFINITY = "∞"
    }
}
