package com.pluto.plugins.preferences.ui

import com.pluto.utilities.views.keyvalue.KeyValuePairEditInputType
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest

internal class EditProcessor private constructor() {

    companion object {
        fun SharedPrefKeyValuePair.toEditorData(): KeyValuePairEditRequest {
            return KeyValuePairEditRequest(
                key = key,
                value = value?.toString(),
                hint = when (value) {
                    is Int, is Long -> "12345"
                    is Boolean -> "true / false"
                    is Float -> "1234.89"
                    else -> "abcde 123"
                },
                inputType = when (value) {
                    is Int, is Long -> KeyValuePairEditInputType.Integer
                    is Float -> KeyValuePairEditInputType.Float
                    is Boolean -> KeyValuePairEditInputType.Boolean
                    else -> KeyValuePairEditInputType.String
                },
                metaData = this
            )
        }

        fun SharedPrefKeyValuePair.fromEditorData(text: String): Any {
            return when (value) {
                is Int -> text.toInt()
                is Long -> text.toLong()
                is Float -> text.toFloat()
                is Boolean -> text.toBoolean()
                else -> text
            }
        }
    }
}