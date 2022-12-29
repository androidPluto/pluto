package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.GravityInt
import com.pluto.utilities.extensions.px2dp

internal class ParserUtils private constructor() {
    companion object {
        fun formatLayoutParam(layoutParamDimen: LayoutParamDimens): String {
            val dp = "${layoutParamDimen.size.toFloat().px2dp.toInt()} dp"
            return when (layoutParamDimen.layoutParam) {
                ViewGroup.LayoutParams.WRAP_CONTENT -> "wrap_content ($dp)"
                ViewGroup.LayoutParams.MATCH_PARENT -> "match_parent ($dp)"
                else -> dp
            }
        }

        fun formatVisibility(value: Int): String = when (value) {
            View.VISIBLE -> "VISIBLE"
            View.INVISIBLE -> "INVISIBLE"
            View.GONE -> "GONE"
            else -> "NOT SET"
        }

        fun formatDrawable(drawable: Drawable?): String? = drawable?.let {
            when (it) {
                is ColorDrawable -> formatColor(it.color)
                else -> it.toString()
            }
        } ?: run {
            null
        }

        fun formatColor(value: Int): String = Integer.toHexString(value).uppercase()

        fun formatGravity(@GravityInt gravity: Int): String = when (gravity) {
            Gravity.NO_GRAVITY -> "NO_GRAVITY"
            Gravity.LEFT -> "LEFT"
            Gravity.TOP -> "TOP"
            Gravity.RIGHT -> "RIGHT"
            Gravity.BOTTOM -> "BOTTOM"
            Gravity.CENTER -> "CENTER"
            Gravity.CENTER_HORIZONTAL -> "CENTER_HORIZONTAL"
            Gravity.CENTER_VERTICAL -> "CENTER_VERTICAL"
            Gravity.START -> "START"
            Gravity.END -> "END"
            Gravity.CLIP_HORIZONTAL -> "CLIP_HORIZONTAL"
            Gravity.CLIP_VERTICAL -> "CLIP_VERTICAL"
            Gravity.FILL -> "FILL"
            Gravity.FILL_HORIZONTAL -> "FILL_HORIZONTAL"
            Gravity.FILL_VERTICAL -> "FILL_VERTICAL"
            else -> "NOT SET"
        }
    }
}
