package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.view.View

interface IParser<T: View> {
    fun getAttrs(view: View): List<Attribute>
}