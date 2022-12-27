package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser

internal class ViewGroupParser : IParser<ViewGroup> {

    override fun getAttrs(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        (view as ViewGroup).apply {
            val childCountDrawAttribute = Attribute("childCount", childCount.toString())
            attributes.add(childCountDrawAttribute)
            val willNotDrawAttribute = Attribute("willNotDraw", willNotDraw().toString())
            attributes.add(willNotDrawAttribute)
        }
        return attributes
    }
}