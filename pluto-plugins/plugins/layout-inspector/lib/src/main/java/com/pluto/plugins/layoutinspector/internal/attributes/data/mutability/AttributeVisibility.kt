package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.view.View
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.views.keyvalue.KeyValuePairEditInputType
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal class AttributeVisibility(title: String, value: Int) : Attribute<Int>(title, value), MutableAttribute {
    override fun displayText(): CharSequence? = formatVisibility(value)

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = title,
        value = formatVisibility(value),
        metaData = this,
        inputType = KeyValuePairEditInputType.Selection,
        candidateOptions = listOf(
            formatVisibility(View.VISIBLE),
            formatVisibility(View.INVISIBLE),
            formatVisibility(View.GONE)
        )
    )

    override fun handleEdit(view: View, updatedValue: String) {
        view.visibility = when (updatedValue) {
            LABEL_VISIBLE -> View.VISIBLE
            LABEL_INVISIBLE -> View.INVISIBLE
            LABEL_GONE -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun formatVisibility(value: Int): String = when (value) {
        View.VISIBLE -> LABEL_VISIBLE
        View.INVISIBLE -> LABEL_INVISIBLE
        View.GONE -> LABEL_GONE
        else -> "NOT SET"
    }

    private companion object {
        const val LABEL_VISIBLE = "VISIBLE"
        const val LABEL_INVISIBLE = "INVISIBLE"
        const val LABEL_GONE = "GONE"
    }
}
