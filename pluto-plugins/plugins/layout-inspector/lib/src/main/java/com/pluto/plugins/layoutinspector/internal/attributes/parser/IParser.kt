package com.pluto.plugins.layoutinspector.internal.attributes.parser

import android.view.View
import java.lang.reflect.ParameterizedType

internal abstract class IParser<T : View> {

    private val parameterizedTypeClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    val parameterizedType: String = parameterizedTypeClass.toString().replace("class", "", true).trim()

    private fun isValidType(viewClazz: Class<*>): Boolean {
        var clazz: Class<*>? = viewClazz
        do {
            if (parameterizedTypeClass === clazz) {
                return true
            }
            clazz = clazz!!.superclass
        } while (clazz != null && clazz != Any::class.java)
        return false
    }

    fun getAttributes(view: View): List<Attribute<*>>? = try {
        if (isValidType(view.javaClass)) getTypeAttributes(view) else null
    } catch (t: Throwable) {
        t.printStackTrace()
        null
    }

    protected abstract fun getTypeAttributes(view: View): List<Attribute<*>>
}