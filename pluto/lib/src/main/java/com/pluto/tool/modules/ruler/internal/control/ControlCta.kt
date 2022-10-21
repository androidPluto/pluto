package com.pluto.tool.modules.ruler.internal.control

import androidx.annotation.DrawableRes
import com.pluto.utilities.list.ListItem

internal data class ControlCta(
    val id: String,
    @DrawableRes val icon: Int,
) : ListItem()
