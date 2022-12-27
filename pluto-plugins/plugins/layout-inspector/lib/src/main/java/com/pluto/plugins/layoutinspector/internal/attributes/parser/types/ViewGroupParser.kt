package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser

internal class ViewGroupParser : IParser<ViewGroup>() {

    override fun getTypeAttributes(view: View): List<Attribute> {
        val attributes = arrayListOf<Attribute>()
        (view as ViewGroup).apply {
            val childCountDrawAttribute = Attribute("child_count", childCount.toString(), parameterizedTypeString)
            attributes.add(childCountDrawAttribute)
            val willNotDrawAttribute = Attribute("willNotDraw", willNotDraw().toString(), parameterizedTypeString)
            attributes.add(willNotDrawAttribute)
        }
        return attributes
    }
}