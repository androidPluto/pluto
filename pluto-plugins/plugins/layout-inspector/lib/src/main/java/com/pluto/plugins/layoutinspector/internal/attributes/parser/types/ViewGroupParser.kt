package com.pluto.plugins.layoutinspector.internal.attributes.parser.types

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.parser.IParser
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeType

internal class ViewGroupParser : IParser<ViewGroup>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as ViewGroup).apply {
            attributes.add(Attribute((AttributeType("child_count")), childCount, parameterizedTypeString))
            attributes.add(Attribute(AttributeType("will_not_draw"), willNotDraw(), parameterizedTypeString))
        }
        return attributes
    }
}