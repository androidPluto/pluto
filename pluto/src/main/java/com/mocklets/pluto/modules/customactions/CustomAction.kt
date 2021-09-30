package com.mocklets.pluto.modules.customactions

import android.view.View
import androidx.annotation.Keep

@Keep
data class CustomAction(
    val title: String,
    val clickListener: View.OnClickListener
)
