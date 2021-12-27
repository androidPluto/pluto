package com.pluto.logger.internal

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.pluto.logger.R
import com.pluto.plugin.utilities.list.ListItem

internal sealed class Level(
    val label: String,
    val color: Int = R.color.pluto___transparent,
    val textColor: Int = R.color.pluto___text_dark_60,
    @DrawableRes val iconRes: Int = 0
) {
    object Verbose : Level("verbose")
    object Debug : Level("debug")
    object Info : Level("info")
    object Warning : Level("warning")
    object Error : Level("error", R.color.pluto___red_05, R.color.pluto___red_80)
    object Event : Level(label = "event", iconRes = R.drawable.pluto_logger___ic_analytics, textColor = R.color.pluto___blue)
}

@Keep
internal data class LogData(
    val level: Level,
    val tag: String,
    val message: String,
    val tr: Throwable? = null,
    val stackTraceElement: StackTraceElement,
    val eventAttributes: HashMap<String, Any?>? = null,
    val timeStamp: Long = System.currentTimeMillis()
) : ListItem()
