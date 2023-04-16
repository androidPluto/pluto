package com.pluto.tool.modules.ruler.internal

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import com.pluto.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.settings.SettingsPreferences

internal data class PaintType(val context: Context) {

    val scale: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    R.color.pluto___red_dark
                } else {
                    R.color.pluto___orange
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    val scaleMarker: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    R.color.pluto___red_80
                } else {
                    R.color.pluto___orange_80
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    val prevScale: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    R.color.pluto___red_60
                } else {
                    R.color.pluto___orange_60
                }
            )
            style = Style.STROKE
            strokeWidth = 1f.dp2px
            pathEffect = DashPathEffect(floatArrayOf(3f.dp, 2f.dp), 0f)
        }
    }

    val measurement: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(R.color.pluto___blue)
            style = Style.FILL
            strokeWidth = 4f.dp2px
            textSize = 14f.dp2px
            typeface = ResourcesCompat.getFont(context, R.font.muli_semibold)
            flags = FAKE_BOLD_TEXT_FLAG
        }
    }

    val boundary: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(R.color.pluto___emerald)
            strokeWidth = 4f.dp2px
            style = Style.STROKE
        }
    }
}
