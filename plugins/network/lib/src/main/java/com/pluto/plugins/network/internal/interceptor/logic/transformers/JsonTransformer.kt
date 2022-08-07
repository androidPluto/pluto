package com.pluto.plugins.network.internal.interceptor.logic.transformers

import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugins.network.internal.interceptor.logic.LOGTAG
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class JsonTransformer : BaseTransformer {

    private val moshi: Moshi = Moshi.Builder().build()
    private val moshiObjectAdapter: JsonAdapter<Map<String, Any?>?> =
        moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
    private val moshiArrayAdapter: JsonAdapter<List<Any?>?> = moshi.adapter(Types.newParameterizedType(List::class.java, Any::class.java))

    @SuppressWarnings("TooGenericExceptionCaught")
    override fun beautify(plain: CharSequence): CharSequence {
        // todo fix this, find a better way to detect array/object
        return try {
            try {
                val jsonObject = moshiObjectAdapter.fromJson(plain.toString())
                moshiObjectAdapter.indent(INDENTATION_TEXT).toJson(jsonObject)
            } catch (e: JsonDataException) {
                DebugLog.e(LOGTAG, "json object conversion failed, trying array conversion")
                val jsonArray = moshiArrayAdapter.fromJson(plain.toString())
                moshiArrayAdapter.indent(INDENTATION_TEXT).toJson(jsonArray)
            }
        } catch (e: Exception) {
            DebugLog.e(LOGTAG, "json parsing failed", e)
            plain
        }
    }

    override fun flatten(plain: CharSequence): String {
        return plain.toString().replace("\n", "").replace("\\s+".toRegex(), "")
    }

    companion object {
        private const val INDENTATION_TEXT = "\t\t"
    }
}
