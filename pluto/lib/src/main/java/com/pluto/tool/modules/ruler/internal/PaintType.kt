package com.pluto.tool.modules.ruler.internal

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import com.pluto.R
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.dp2px

/**
 * Class that provides various Paint objects for drawing the ruler components.
 *
 * This class encapsulates the different Paint configurations needed for the ruler tool,
 * including paints for the scale lines, markers, previous scale position, measurements,
 * and boundary. Each paint is configured with appropriate colors, styles, and effects
 * based on the current theme setting.
 *
 * @param context The context used to access resources and settings
 */
internal data class PaintType(val context: Context) {

    /**
     * Paint for the main scale lines of the ruler.
     * The color adapts based on the current theme setting.
     */
    val scale: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    com.pluto.plugin.R.color.pluto___red_dark
                } else {
                    com.pluto.plugin.R.color.pluto___orange
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    /**
     * Paint for the scale markers (ticks) on the ruler.
     * These are the small lines that indicate measurement units.
     */
    val scaleMarker: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    com.pluto.plugin.R.color.pluto___red_80
                } else {
                    com.pluto.plugin.R.color.pluto___orange_80
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    /**
     * Paint for the previous scale position, shown as a dashed line.
     * This helps users see where the scale was before moving it.
     */
    val prevScale: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    com.pluto.plugin.R.color.pluto___red_60
                } else {
                    com.pluto.plugin.R.color.pluto___orange_60
                }
            )
            style = Style.STROKE
            strokeWidth = 1f.dp2px
            pathEffect = DashPathEffect(floatArrayOf(3f.dp, 2f.dp), 0f)
        }
    }

    /**
     * Paint for drawing measurement text and lines.
     * This is used to display the actual measurement values and the measurement line.
     */
    val measurement: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    com.pluto.plugin.R.color.pluto___blue
                } else {
                    com.pluto.plugin.R.color.pluto___teal
                }
            )
            style = Style.FILL
            strokeWidth = 4f.dp2px
            textSize = 14f.dp2px
            typeface = ResourcesCompat.getFont(context, com.pluto.plugin.R.font.muli_semibold)
            flags = FAKE_BOLD_TEXT_FLAG
        }
    }

    /**
     * Paint for drawing the boundary of the ruler view.
     * This helps visually define the edges of the ruler area.
     */
    val boundary: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(com.pluto.plugin.R.color.pluto___emerald)
            strokeWidth = 4f.dp2px
            style = Style.STROKE
        }
    }
}
