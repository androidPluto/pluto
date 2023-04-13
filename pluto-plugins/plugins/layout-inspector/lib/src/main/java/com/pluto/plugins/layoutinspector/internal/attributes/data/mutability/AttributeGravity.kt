package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.view.Gravity
import androidx.annotation.GravityInt
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute

internal class AttributeGravity(title: String, value: Int) : Attribute<Int>(title, value) {
    override fun displayText(): CharSequence? = formatGravity(value)

//    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
//        key = title,
//        value = formatGravity(value),
//        metaData = this,
//        inputType = KeyValuePairEditInputType.Selection,
//        candidateOptions = listOf(
//            LABEL_NO_GRAVITY,
//            LABEL_LEFT,
//            LABEL_TOP,
//            LABEL_RIGHT,
//            LABEL_BOTTOM,
//            LABEL_CENTER,
//            LABEL_START,
//            LABEL_END,
//            LABEL_CENTER_HORIZONTAL,
//            LABEL_CENTER_VERTICAL,
//            LABEL_CLIP_HORIZONTAL,
//            LABEL_CLIP_VERTICAL,
//            LABEL_FILL_HORIZONTAL,
//            LABEL_FILL_VERTICAL,
//            LABEL_FILL,
//            LABEL_NOT_SET
//        )
//    )
//
//    override fun handleEdit(view: View, updatedValue: String) {
//        var gravity: Int? = null
//        when (updatedValue) {
//            LABEL_NO_GRAVITY -> "NO_GRAVITY"
//            LABEL_LEFT -> "LEFT"
//            LABEL_TOP -> "TOP"
//            LABEL_RIGHT -> "RIGHT"
//            LABEL_BOTTOM -> "BOTTOM"
//            LABEL_CENTER -> "CENTER"
//            LABEL_CENTER_HORIZONTAL -> "CENTER_HORIZONTAL"
//            LABEL_CENTER_VERTICAL -> "CENTER_VERTICAL"
//            LABEL_START -> "START"
//            LABEL_END -> "END"
//            LABEL_CLIP_HORIZONTAL -> "CLIP_HORIZONTAL"
//            LABEL_CLIP_VERTICAL -> "CLIP_VERTICAL"
//            LABEL_FILL -> "FILL"
//            LABEL_FILL_HORIZONTAL -> "FILL_HORIZONTAL"
//            LABEL_FILL_VERTICAL -> "FILL_VERTICAL"
//            else -> "NOT SET"
//        }
//
//        if(view is TextView) {
//            Log.d("prateek")
//            view.gravity = Gravity.LEFT
//        }
//        if (scaleType != null) {
//            (view as ImageView).scaleType = scaleType
//        } else {
//            Log.e("layout-inspector", "improper scale type value, should be between 0f to 1f")
//        }
//    }

    @SuppressWarnings("ComplexMethod")
    private fun formatGravity(@GravityInt gravity: Int): String = when (gravity) {
        Gravity.NO_GRAVITY -> LABEL_NO_GRAVITY
        Gravity.LEFT -> LABEL_LEFT
        Gravity.TOP -> LABEL_TOP
        Gravity.RIGHT -> LABEL_RIGHT
        Gravity.BOTTOM -> LABEL_BOTTOM
        Gravity.CENTER -> LABEL_CENTER
        Gravity.CENTER_HORIZONTAL -> LABEL_CENTER_HORIZONTAL
        Gravity.CENTER_VERTICAL -> LABEL_CENTER_VERTICAL
        Gravity.START -> LABEL_START
        Gravity.END -> LABEL_END
        Gravity.CLIP_HORIZONTAL -> LABEL_CLIP_HORIZONTAL
        Gravity.CLIP_VERTICAL -> LABEL_CLIP_VERTICAL
        Gravity.FILL -> LABEL_FILL
        Gravity.FILL_HORIZONTAL -> LABEL_FILL_HORIZONTAL
        Gravity.FILL_VERTICAL -> LABEL_FILL_VERTICAL
        else -> LABEL_NOT_SET
    }

    private companion object {
        const val LABEL_NO_GRAVITY = "NO_GRAVITY"
        const val LABEL_LEFT = "LEFT"
        const val LABEL_TOP = "TOP"
        const val LABEL_RIGHT = "RIGHT"
        const val LABEL_BOTTOM = "BOTTOM"
        const val LABEL_CENTER = "CENTER"
        const val LABEL_CENTER_HORIZONTAL = "CENTER_HORIZONTAL"
        const val LABEL_CENTER_VERTICAL = "CENTER_VERTICAL"
        const val LABEL_START = "START"
        const val LABEL_END = "END"
        const val LABEL_CLIP_HORIZONTAL = "CLIP_HORIZONTAL"
        const val LABEL_CLIP_VERTICAL = "CLIP_VERTICAL"
        const val LABEL_FILL = "FILL"
        const val LABEL_FILL_HORIZONTAL = "FILL_HORIZONTAL"
        const val LABEL_FILL_VERTICAL = "FILL_VERTICAL"
        const val LABEL_NOT_SET = "NOT SET"
    }
}
