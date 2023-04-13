package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal abstract class AttributeText(title: String, value: CharSequence?) : Attribute<CharSequence?>(title, value), MutableAttribute {

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = title, value = value?.toString(), hint = "enter value", metaData = this
    )

    class Text(title: String, value: CharSequence?) : AttributeText(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            if (view is TextView) {
                view.text = updatedValue
            }
        }
    }

    class Hint(title: String, value: CharSequence?) : AttributeText(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            if (view is TextView) {
                view.hint = updatedValue
            }
        }
    }
}
