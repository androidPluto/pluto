package com.pluto.plugins.layoutinspector.internal.attributes.data.mutability

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.views.keyvalue.KeyValuePairEditInputType
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal abstract class AttributeDimenDP(title: String, value: Float) : Attribute<Float>(title, value), MutableAttribute {
    override fun displayText(): CharSequence? = "${value.px2dp.toInt()} dp"

    override fun requestEdit(): KeyValuePairEditRequest = KeyValuePairEditRequest(
        key = "$title (dp)", value = "${value.px2dp.toInt()}", hint = "enter value (in dp)", inputType = KeyValuePairEditInputType.Integer, metaData = this
    )

    /* padding */
    class PaddingStart(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            view.setPaddingRelative(
                updatedValue.toFloat().dp2px.toInt(), view.paddingTop, view.paddingEnd, view.paddingBottom
            )
        }
    }

    class PaddingEnd(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            view.setPaddingRelative(
                view.paddingStart, view.paddingTop, updatedValue.toFloat().dp2px.toInt(), view.paddingBottom
            )
        }
    }

    class PaddingTop(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            view.setPaddingRelative(
                view.paddingStart, updatedValue.toFloat().dp2px.toInt(), view.paddingEnd, view.paddingBottom
            )
        }
    }

    class PaddingBottom(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            view.setPaddingRelative(
                view.paddingStart, view.paddingTop, view.paddingEnd, updatedValue.toFloat().dp2px.toInt(),
            )
        }
    }

    /* margin */
    class MarginStart(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).marginStart = updatedValue.toFloat().dp2px.toInt()
        }
    }

    class MarginEnd(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = updatedValue.toFloat().dp2px.toInt()
        }
    }

    class MarginTop(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).topMargin = updatedValue.toFloat().dp2px.toInt()
        }
    }

    class MarginBottom(title: String, value: Float) : AttributeDimenDP(title, value) {
        override fun handleEdit(view: View, updatedValue: String) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = updatedValue.toFloat().dp2px.toInt()
        }
    }
}