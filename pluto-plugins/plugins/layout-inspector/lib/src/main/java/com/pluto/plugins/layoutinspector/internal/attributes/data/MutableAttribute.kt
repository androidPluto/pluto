package com.pluto.plugins.layoutinspector.internal.attributes.data

import android.view.View
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

interface MutableAttribute {
    fun requestEdit(): KeyValuePairEditRequest
    fun handleEdit(view: View, updatedValue: String)
}
