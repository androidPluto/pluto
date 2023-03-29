package com.pluto.plugins.layoutinspector.internal.attributes.data.parser.types

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.data.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.data.parser.IParser

internal class ViewGroupParser : IParser<ViewGroup>() {

    override fun getTypeAttributes(view: View): List<Attribute<*>> {
        val attributes = arrayListOf<Attribute<*>>()
        (view as ViewGroup).apply {
            attributes.add(Attribute(("child_count"), childCount))
            attributes.add(Attribute("will_not_draw", willNotDraw()))
        }
        return attributes
    }
}
