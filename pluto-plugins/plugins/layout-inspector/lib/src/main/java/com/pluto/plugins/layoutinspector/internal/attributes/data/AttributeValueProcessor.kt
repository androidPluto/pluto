package com.pluto.plugins.layoutinspector.internal.attributes.data

import android.widget.ImageView
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.extensions.px2sp
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal fun <T> AttributeTag.toDisplayText(value: T?): CharSequence? {
    return when(this) {
        is AttributeTag.LayoutParams -> (value as LayoutParamDimens).toString()
        is AttributeTag.Dimension -> "${(value as Float).px2dp.toInt()} dp"
        is AttributeTag.Color -> Utils.formatColor(value as Int)
        AttributeTag.ScaleType -> (value as ImageView.ScaleType).name
        AttributeTag.Visibility -> Utils.formatVisibility(value as Int)
        AttributeTag.Immutable.Gravity -> Utils.formatGravity(value as Int)
        AttributeTag.TextSize -> "${(value as Float).px2sp.toInt()} sp"
        else -> value?.toString()
    }
}

internal fun <T> AttributeTag.toEditorRequestData(title: String, value: T): KeyValuePairEditRequest? {
    return null
}

internal fun AttributeTag.onValueUpdated(title: String, updatedValue: String) {

}