package com.mocklets.pluto.modules.network.transformers

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.modules.network.LOGTAG
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

internal class JsonBaseTransformer : BaseTransformer {
    override fun beautify(plain: CharSequence, indent: Int): CharSequence? {
        return try {
            val je: JsonElement = JsonParser.parseString(plain.toString())
            when {
                je.isJsonArray -> JSONArray(plain.toString()).toString(indent)
                je.isJsonObject -> JSONObject(plain.toString()).toString(indent)
                else -> plain
            }
        } catch (e: JsonSyntaxException) {
            DebugLog.e(LOGTAG, "json parsing failed", e)
            plain
        } catch (e: JSONException) {
            DebugLog.e(LOGTAG, "json parsing failed", e)
            plain
        }
    }

    override fun flatten(plain: CharSequence): String? {
        return plain.toString()
    }
}
