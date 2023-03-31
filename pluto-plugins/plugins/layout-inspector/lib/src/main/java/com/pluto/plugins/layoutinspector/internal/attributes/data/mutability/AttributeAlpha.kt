package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.util.Log
import android.view.View
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal class AttributeAlpha(title: String, value: Float) : Attribute<Float>(title, value), MutableAttribute {

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = "$title (0 to 1)", value = value.toString(), hint = "enter value (0.0 - 1.0)", metaData = this
    )

    override fun handleEdit(view: View, updatedValue: String) {
        if (updatedValue.toFloat() in 0f..1f) {
            view.alpha = updatedValue.toFloat()
        } else {
            Log.e("layout-inspector", "improper alpha value, should be between 0f to 1f")
        }
    }
}