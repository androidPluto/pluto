package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.view.View
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.ImageViewParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.TextViewParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.ViewGroupParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.types.ViewParser
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class AttrParser {
    private val parsers = arrayListOf<IParser<*>>().apply {
        add(ImageViewParser())
        add(TextViewParser())
        add(ViewGroupParser())
        add(ViewParser())
    }

    fun addParser(parser: IParser<*>) {
        parsers.add(0, parser)
    }

    fun parse(v: View): List<Attribute> {
        val attributes: MutableList<Attribute> = ArrayList()
        for (parser in parsers) {
            try {
                val parameterizedType = parser.javaClass.genericInterfaces[0] as ParameterizedType
                val actualTypeArguments = parameterizedType.actualTypeArguments[0]
                if (findUpUntilEqual(v.javaClass, actualTypeArguments)) {
                    val result = parser.getAttrs(v)
                    if (result.isNotEmpty()) {
                        for (i in result.indices) {
                            result[i].category = actualTypeArguments.toString().replace("class", "", true).trim()
                        }
                        attributes.addAll(result)
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        return attributes
    }

    private fun findUpUntilEqual(clazz: Class<*>, type: Type): Boolean {
        var clazz: Class<*>? = clazz
        do {
            if (type === clazz) {
                return true
            }
            clazz = clazz!!.superclass
        } while (clazz != null && clazz != Any::class.java)
        return false
    }
}
