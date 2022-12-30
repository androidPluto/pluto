package com.pluto.plugins.layoutinspector.internal.inspect

import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Method

internal object Reflect28Util {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                val classClazz: Class<*> = Class::class.java
                val classLoaderField: Field = classClazz.getDeclaredField("classLoader")
                classLoaderField.isAccessible = true
                classLoaderField.set(Reflect28Util::class.java, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(ClassNotFoundException::class)
    fun forName(className: String): Class<*> {
        return Class.forName(className)
    }

    @Throws(NoSuchFieldException::class)
    fun getDeclaredField(clz: Class<*>, name: String): Field {
        return clz.getDeclaredField(name)
    }

    @Throws(NoSuchMethodException::class)
    fun getDeclaredMethod(clz: Class<*>, name: String, vararg parameterType: Class<*>?): Method {
        return clz.getDeclaredMethod(name, *parameterType)
    }
}
