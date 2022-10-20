package com.pluto.utilities.views.keyvalue

import androidx.annotation.Keep

@Keep
data class KeyValuePairData(
    val key: String,
    val value: CharSequence?,
    val iconStart: Int? = null,
    val showClickIndicator: Boolean = false,
    val isClickable: Boolean = false,
    val onClick: (() -> Unit)? = null
)
