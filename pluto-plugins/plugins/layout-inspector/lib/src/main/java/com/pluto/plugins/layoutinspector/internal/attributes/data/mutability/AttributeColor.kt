package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal abstract class AttributeColor(title: String, value: Int) : Attribute<Int>(title, value), MutableAttribute {
    override fun displayText(): CharSequence? = formatColor(value)

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = title, value = formatColor(value), hint = "enter value", metaData = this
    )

    class Text(title: String, value: Int) : AttributeColor(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            if(view is TextView) {
                view.setTextColor(Color.parseColor(updatedValue))
            }
        }
    }

    class Hint(title: String, value: Int) : AttributeColor(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            if(view is TextView) {
                view.setHintTextColor(Color.parseColor(updatedValue))
            }
        }
    }

    private fun formatColor(value: Int): String = "#${Integer.toHexString(value).uppercase()}"
}