package com.pluto.plugins.network.base.internal.interceptor.logic.transformers

import com.pluto.plugins.network.base.internal.interceptor.logic.LOGTAG
import com.pluto.utilities.DebugLog
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class JsonTransformer : BaseTransformer {

    private val moshi: Moshi = Moshi.Builder().add(AnyJsonAdapter()).build()
    private val moshiAnyAdapter: JsonAdapter<Any> = moshi.adapter(Any::class.java)

    @SuppressWarnings("TooGenericExceptionCaught")
    override fun beautify(plain: CharSequence): CharSequence {
        return try {
            val jsonObject = moshiAnyAdapter.fromJson(plain.toString())
            moshiAnyAdapter.indent(INDENTATION_TEXT).toJson(jsonObject)
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
