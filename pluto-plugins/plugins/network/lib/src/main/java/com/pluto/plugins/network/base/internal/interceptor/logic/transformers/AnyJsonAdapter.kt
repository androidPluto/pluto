package com.pluto.plugins.network.base.internal.interceptor.logic.transformers

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.internal.Util
import java.io.IOException

internal class AnyJsonAdapter(
    private val moshi: Moshi = Moshi.Builder().build()
) : JsonAdapter<Any?>() {

    @Throws(IOException::class)
    @FromJson
    override fun fromJson(reader: JsonReader): Any? {
        return reader.readJsonValueNew()
    }
    @Throws(IOException::class)
    @ToJson
    override fun toJson(writer: JsonWriter, value: Any?) {
        val valueClass: Class<*> = value!!.javaClass
        if (valueClass == Any::class.java) {
            // Don't recurse infinitely when the runtime type is also Object.class.
            writer.beginObject()
            writer.endObject()
        } else {
            moshi.adapter<Any>(toJsonType(valueClass), Util.NO_ANNOTATIONS).toJson(writer, value)
        }
    }

    private fun JsonReader.nextNumber(): Number {
        try {
            return nextInt()
        } catch (ignored: Throwable) {
            ignored.printStackTrace()
        }

        try {
            return nextLong()
        } catch (ignored: Throwable) {
            ignored.printStackTrace()
        }

        return nextDouble()
    }

    /**
     * Returns the type to look up a type adapter for when writing `value` to JSON. Without
     * this, attempts to emit standard types like `LinkedHashMap` would fail because Moshi doesn't
     * provide built-in adapters for implementation types. It knows how to **write**
     * those types, but lacks a mechanism to read them because it doesn't know how to find the
     * appropriate constructor.
     */
    private fun toJsonType(valueClass: Class<*>): Class<*> {
        if (MutableMap::class.java.isAssignableFrom(valueClass)) return MutableMap::class.java
        return if (Collection::class.java.isAssignableFrom(valueClass)) Collection::class.java else valueClass
    }

    /**
     * copied from JsonReader.readJsonValue()
     * updated JsonReader.Token.NUMBER case to try parsing as int, long, and double sequentially
     */
    @Throws(IOException::class)
    fun JsonReader.readJsonValueNew(): Any? {
        when (peek()) {
            JsonReader.Token.BEGIN_ARRAY -> {
                val list: MutableList<Any?> = ArrayList()
                beginArray()
                while (hasNext()) {
                    list.add(readJsonValueNew())
                }
                endArray()
                return list
            }

            JsonReader.Token.BEGIN_OBJECT -> {
                val map: MutableMap<String, Any?> = HashMap()
                beginObject()
                while (hasNext()) {
                    val name: String = nextName()
                    val value = readJsonValueNew()
                    val replaced = map.put(name, value)
                    if (replaced != null) {
                        throw JsonDataException(
                            "Map key '" +
                                name +
                                "' has multiple values at path " +
                                getPath() +
                                ": " +
                                replaced +
                                " and " +
                                value
                        )
                    }
                }
                endObject()
                return map
            }

            JsonReader.Token.STRING -> return nextString()
            JsonReader.Token.NUMBER -> return nextNumber()
            JsonReader.Token.BOOLEAN -> return nextBoolean()
            JsonReader.Token.NULL -> return nextNull<Any>()
            else -> throw java.lang.IllegalStateException(
                "Expected a value but was " + peek() + " at path " + getPath()
            )
        }
    }
}
