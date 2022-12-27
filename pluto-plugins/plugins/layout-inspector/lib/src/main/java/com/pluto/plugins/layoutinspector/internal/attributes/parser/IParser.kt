package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.view.View

internal interface IParser<T: View> {
    fun getAttrs(view: View): List<Attribute>
}