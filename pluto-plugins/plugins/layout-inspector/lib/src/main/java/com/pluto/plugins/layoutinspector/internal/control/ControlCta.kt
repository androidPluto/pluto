package com.pluto.plugins.layoutinspector.internal.control

import androidx.annotation.DrawableRes
import com.pluto.utilities.list.ListItem

internal data class ControlCta(
    val id: String,
    @DrawableRes val icon: Int,
    val hint: String
) : ListItem()
