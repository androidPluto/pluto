package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.views.keyvalue.KeyValuePairEditInputType
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal abstract class AttributeLayoutParam(title: String, value: Data) : Attribute<AttributeLayoutParam.Data>(title, value), MutableAttribute {
    override fun displayText(): CharSequence? = value.toDisplayText()
    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = "$title (dp)",
        value = if (value.actualValue() != LABEL_WRAP_CONTENT && value.actualValue() != LABEL_MATCH_PARENT) {
            value.actualValue()
        } else {
            null
        },
        hint = "enter value (in dp)",
        metaData = this,
        inputType = KeyValuePairEditInputType.Integer,
        candidateOptions = listOf(
            LABEL_WRAP_CONTENT,
            LABEL_MATCH_PARENT
        )
    )

    class Height(title: String, value: Data): AttributeLayoutParam(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            view.layoutParams.height = when (updatedValue) {
                LABEL_WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT
                LABEL_MATCH_PARENT -> ViewGroup.LayoutParams.MATCH_PARENT
                else -> updatedValue.toFloat().dp2px.toInt()
            }
        }
    }

    class Width(title: String, value: Data): AttributeLayoutParam(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            view.layoutParams.width = when (updatedValue) {
                LABEL_WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT
                LABEL_MATCH_PARENT -> ViewGroup.LayoutParams.MATCH_PARENT
                else -> updatedValue.toFloat().dp2px.toInt()
            }
        }
    }

    private companion object {
        const val LABEL_WRAP_CONTENT = "wrap_content"
        const val LABEL_MATCH_PARENT = "match_parent"
    }

    internal data class Data(
        val layoutParam: Int,
        val size: Int
    ) {
        fun toDisplayText(): CharSequence {
            val dp = "${size.toFloat().px2dp.toInt()} dp"
            return when (layoutParam) {
                ViewGroup.LayoutParams.WRAP_CONTENT -> "$LABEL_WRAP_CONTENT ($dp)"
                ViewGroup.LayoutParams.MATCH_PARENT -> "$LABEL_MATCH_PARENT ($dp)"
                else -> dp
            }
        }

        fun actualValue(): String = when (layoutParam) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> LABEL_WRAP_CONTENT
            ViewGroup.LayoutParams.MATCH_PARENT -> LABEL_MATCH_PARENT
            else -> "${size.toFloat().px2dp.toInt()}"
        }
    }
}