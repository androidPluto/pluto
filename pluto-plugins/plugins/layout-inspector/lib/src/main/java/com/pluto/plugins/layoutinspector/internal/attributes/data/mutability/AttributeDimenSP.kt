package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.extensions.px2sp
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal abstract class AttributeDimenSP(title: String, value: Float) : Attribute<Float>(title, value), MutableAttribute {
    override fun displayText(): CharSequence? = "${value.px2sp.toInt()} sp"

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = "$title (sp)", value = value.px2sp.toInt().toString(), hint = "enter value (in sp)", metaData = this
    )

    class TextSize(title: String, value: Float) : AttributeDimenSP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            if (view is TextView) {
                view.textSize = updatedValue.toFloat()
            }
        }
    }
}
