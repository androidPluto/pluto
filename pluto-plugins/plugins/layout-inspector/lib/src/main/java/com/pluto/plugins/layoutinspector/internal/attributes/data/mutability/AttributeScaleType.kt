package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.views.keyvalue.KeyValuePairEditInputType
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal class AttributeScaleType(title: String, value: ImageView.ScaleType) : Attribute<ImageView.ScaleType>(title, value), MutableAttribute {
    override fun displayText(): CharSequence? = value.name

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = title,
        value = value.name,
        metaData = this,
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

    override fun handleEdit(view: View, updatedValue: String) {
        var scaleType: ImageView.ScaleType? = null
        when (updatedValue) {
            LABEL_CENTER_INSIDE -> scaleType = ImageView.ScaleType.CENTER_INSIDE
            LABEL_CENTER_CROP -> scaleType = ImageView.ScaleType.CENTER_CROP
            LABEL_CENTER -> scaleType = ImageView.ScaleType.CENTER
            LABEL_FIT_CENTER -> scaleType = ImageView.ScaleType.FIT_CENTER
            LABEL_FIT_END -> scaleType = ImageView.ScaleType.FIT_END
            LABEL_FIT_START -> scaleType = ImageView.ScaleType.FIT_START
            LABEL_FIT_XY -> scaleType = ImageView.ScaleType.FIT_XY
            LABEL_MATRIX -> scaleType = ImageView.ScaleType.MATRIX
        }
        if (scaleType != null) {
            (view as ImageView).scaleType = scaleType
        } else {
            Log.e("layout-inspector", "improper scale type value, should be between 0f to 1f")
        }
    }

    private companion object {
        const val LABEL_CENTER_INSIDE = "CENTER_INSIDE"
        const val LABEL_CENTER_CROP = "CENTER_CROP"
        const val LABEL_CENTER = "CENTER"
        const val LABEL_FIT_CENTER = "FIT_CENTER"
        const val LABEL_FIT_END = "FIT_END"
        const val LABEL_FIT_START = "FIT_START"
        const val LABEL_FIT_XY = "FIT_XY"
        const val LABEL_MATRIX = "MATRIX"
    }
}