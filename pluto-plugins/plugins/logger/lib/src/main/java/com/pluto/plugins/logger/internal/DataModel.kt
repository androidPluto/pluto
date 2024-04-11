package com.pluto.plugins.logger.internal

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.pluto.plugins.logger.R
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorOption
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
internal open class Level(
    val label: String,
    val color: Int = R.color.pluto___transparent,
    val textColor: Int = R.color.pluto___text_dark_60,
    @DrawableRes val iconRes: Int = 0
) {
    object Verbose : Level("verbose")
    object Debug : Level("debug")
    object Info : Level("info")
    object Warning : Level("warning")
    object WTF : Level("wtf")
    object Error : Level("error", R.color.pluto___red_05, R.color.pluto___red_80)
    object Event : Level(
        label = "event",
        iconRes = R.drawable.pluto_logger___ic_analytics,
        textColor = R.color.pluto___blue
    )
}

@Keep
@JsonClass(generateAdapter = true)
internal data class LogData(
    val level: Level,
    val tag: String,
    val message: String,
    val tr: ExceptionData? = null,
    val stackTrace: StackTrace,
    val eventAttributes: Map<String, Any?>? = null,
    val timeStamp: Long = System.currentTimeMillis()
) : ListItem()

@Keep
internal data class LogPreviousSessionHeader(
    val label: String = "header"
) : ListItem()

@Keep
@JsonClass(generateAdapter = true)
internal data class StackTrace(
    val methodName: String,
    val fileName: String,
    val lineNumber: Int,
)

@Keep
@JsonClass(generateAdapter = true)
internal data class LogType(
    val type: String
) : SelectorOption() {
    override fun displayText(): CharSequence {
        return type
    }
}

@Keep
@JsonClass(generateAdapter = true)
internal data class LogTimeStamp(
    val timeStamp: Int = 0,
    val isSessionFilter: Boolean = false
) : SelectorOption() {
    override fun displayText(): CharSequence {
        if (isSessionFilter) {
            return "Current session only"
        }
        if (timeStamp == 1) {
            return "< $timeStamp minute"
        }
        return "< $timeStamp minutes"
    }
}
