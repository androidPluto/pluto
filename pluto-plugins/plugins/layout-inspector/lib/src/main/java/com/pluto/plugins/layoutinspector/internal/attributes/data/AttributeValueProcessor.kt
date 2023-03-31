package com.pluto.plugins.layoutinspector.internal.attributes.data

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.extensions.px2sp
import com.pluto.utilities.views.keyvalue.KeyValuePairEditInputType
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest


internal fun <T> AttributeTag.toDisplayText(value: T?): CharSequence? {
    return when (this) {
        is AttributeTag.LayoutParams -> (value as LayoutParamDimens).toString()
        is AttributeTag.Dimension -> "${(value as Float).px2dp.toInt()} dp"
        is AttributeTag.Color -> Utils.formatColor(value as Int)
        AttributeTag.ScaleType -> (value as ImageView.ScaleType).name
        AttributeTag.Visibility -> Utils.formatVisibility(value as Int)
        AttributeTag.TextSize -> "${(value as Float).px2sp.toInt()} sp"
        else -> value?.toString()
    }
}

internal fun <T> Attribute<T>.toEditorRequestData(title: String, value: T?): KeyValuePairEditRequest? {
    return when (this.tag) {
        is AttributeTag.Dimension -> KeyValuePairEditRequest(
            key = "$title (dp)",
            value = (value as Float).px2dp.toInt().toString(),
            hint = "enter value (in dp)",
            inputType = KeyValuePairEditInputType.Integer,
            metaData = this.tag
        )

        is AttributeTag.Text, AttributeTag.Hint -> KeyValuePairEditRequest(
            key = title, value = value?.toString(), hint = "enter value", metaData = this.tag
        )

        is AttributeTag.TextSize -> KeyValuePairEditRequest(
            key = "$title (sp)", value = (value as Float).px2sp.toInt().toString(), hint = "enter value (in sp)", metaData = this.tag
        )

        is AttributeTag.Color -> KeyValuePairEditRequest(
            key = title, value = Utils.formatColor(value as Int), hint = "enter value", metaData = this.tag
        )

        is AttributeTag.Alpha -> KeyValuePairEditRequest(
            key = "$title (0 to 1)", value = value?.toString(), hint = "enter value (0.0 - 1.0)", metaData = this.tag
        )

        is AttributeTag.Visibility -> KeyValuePairEditRequest(
            key = title,
            value = Utils.formatVisibility(value as Int),
            metaData = this.tag,
            inputType = KeyValuePairEditInputType.Selection,
            candidateOptions = listOf(
                Utils.formatVisibility(View.VISIBLE).uppercase(),
                Utils.formatVisibility(View.GONE).uppercase(),
                Utils.formatVisibility(View.INVISIBLE).uppercase()
            )
        )

        is AttributeTag.ScaleType -> KeyValuePairEditRequest(
            key = title,
            value = (value as ImageView.ScaleType).name,
            metaData = this.tag,
            inputType = KeyValuePairEditInputType.Selection,
            candidateOptions = listOf(
                ImageView.ScaleType.CENTER_INSIDE.name,
                ImageView.ScaleType.CENTER_CROP.name,
                ImageView.ScaleType.CENTER.name,
                ImageView.ScaleType.FIT_CENTER.name,
                ImageView.ScaleType.FIT_END.name,
                ImageView.ScaleType.FIT_START.name,
                ImageView.ScaleType.FIT_XY.name,
                ImageView.ScaleType.MATRIX.name
            )
        )

        is AttributeTag.LayoutParams -> KeyValuePairEditRequest(
            key = "$title (dp)",
            value = if ((value as LayoutParamDimens).actualValue() != "wrap_content" && (value as LayoutParamDimens).actualValue() != "match_parent") {
                (value as LayoutParamDimens).actualValue()
            } else {
                null
            },
            hint = "enter value (in dp)",
            metaData = tag,
            inputType = KeyValuePairEditInputType.Integer,
            candidateOptions = listOf(
                "wrap_content".uppercase(),
                "match_parent".uppercase()
            )

        )

        else -> null
    }
}

internal fun AttributeTag.updateValue(view: View, updatedValue: String) {
    when (this) {
        /* margins */
        AttributeTag.Dimension.MarginStart -> (view.layoutParams as MarginLayoutParams).marginStart = updatedValue.toFloat().dp2px.toInt()
        AttributeTag.Dimension.MarginEnd -> (view.layoutParams as MarginLayoutParams).marginEnd = updatedValue.toFloat().dp2px.toInt()
        AttributeTag.Dimension.MarginTop -> (view.layoutParams as MarginLayoutParams).topMargin = updatedValue.toFloat().dp2px.toInt()
        AttributeTag.Dimension.MarginBottom -> (view.layoutParams as MarginLayoutParams).bottomMargin = updatedValue.toFloat().dp2px.toInt()

        /* padding */
        AttributeTag.Dimension.PaddingStart -> view.setPaddingRelative(
            updatedValue.toFloat().dp2px.toInt(), view.paddingTop, view.paddingEnd, view.paddingBottom
        )

        AttributeTag.Dimension.PaddingEnd -> view.setPaddingRelative(
            view.paddingStart, view.paddingTop, updatedValue.toFloat().dp2px.toInt(), view.paddingBottom
        )

        AttributeTag.Dimension.PaddingTop -> view.setPaddingRelative(
            view.paddingStart, updatedValue.toFloat().dp2px.toInt(), view.paddingEnd, view.paddingBottom
        )

        AttributeTag.Dimension.PaddingBottom -> view.setPaddingRelative(
            view.paddingStart,
            view.paddingTop,
            view.paddingEnd,
            updatedValue.toFloat().dp2px.toInt(),
        )

        AttributeTag.Alpha -> if (updatedValue.toFloat() in 0f..1f) {
            view.alpha = updatedValue.toFloat()
        } else {
            Log.e("layout-inspector", "improper alpha value, should be between 0f to 1f")
        }

        AttributeTag.Visibility -> {
            view.visibility = when (updatedValue) {
                "VISIBLE" -> View.VISIBLE
                "GONE" -> View.GONE
                "INVISIBLE" -> View.INVISIBLE
                else -> View.VISIBLE
            }
        }

        AttributeTag.ScaleType -> {
            var scaleType: ImageView.ScaleType? = null
            when (updatedValue) {
                "CENTER_INSIDE" -> scaleType = ImageView.ScaleType.CENTER_INSIDE
                "CENTER_CROP" -> scaleType = ImageView.ScaleType.CENTER_CROP
                "CENTER" -> scaleType = ImageView.ScaleType.CENTER
                "FIT_CENTER" -> scaleType = ImageView.ScaleType.FIT_CENTER
                "FIT_END" -> scaleType = ImageView.ScaleType.FIT_END
                "FIT_START" -> scaleType = ImageView.ScaleType.FIT_START
                "FIT_XY" -> scaleType = ImageView.ScaleType.FIT_XY
                "MATRIX" -> scaleType = ImageView.ScaleType.MATRIX
            }
            if (scaleType != null) {
                (view as ImageView).scaleType = scaleType
            } else {
                Log.e("layout-inspector", "improper scale type value, should be between 0f to 1f")
            }
        }

        /* layout params */
        AttributeTag.LayoutParams.Height -> {
            view.layoutParams.height = when (updatedValue.lowercase()) {
                "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
                "match_parent" -> ViewGroup.LayoutParams.MATCH_PARENT
                else -> updatedValue.toFloat().dp2px.toInt()
            }
        }

        AttributeTag.LayoutParams.Width -> {
            view.layoutParams.width = when (updatedValue.lowercase()) {
                "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
                "match_parent" -> ViewGroup.LayoutParams.MATCH_PARENT
                else -> updatedValue.toFloat().dp2px.toInt()
            }
        }

        /* text */
        AttributeTag.Text -> (view as TextView).text = updatedValue
        AttributeTag.Hint -> (view as TextView).hint = updatedValue
        AttributeTag.TextSize -> (view as TextView).textSize = updatedValue.toFloat()
        AttributeTag.Color.Text -> (view as TextView).setTextColor(Color.parseColor(updatedValue))
        AttributeTag.Color.Hint -> (view as TextView).setHintTextColor(Color.parseColor(updatedValue))
    }
}