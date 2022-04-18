package com.pluto.plugins.datastore.pref.internal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PrefUiModel(
    val name: String,
    val data: List<PrefElement>,
    val isExpanded: MutableState<Boolean> = mutableStateOf(true)
)

data class PrefElement(
    val prefName: String,
    val key: String,
    val value: String,
    val type: Type
)

sealed class Type(val displayText: String) {

    object TypeString : Type("string")
    object TypeBoolean : Type("boolean")
    object TypeDouble : Type("double")
    object TypeFloat : Type("float")
    object TypeLong : Type("long")
    object TypeUnknown : Type("unknown")

    companion object {
        fun <K> type(obj: K) = when (obj) {
            is String -> TypeString
            is Boolean -> TypeBoolean
            is Double -> TypeDouble
            is Long -> TypeLong
            is Float -> TypeFloat
            else -> TypeUnknown
        }
    }
}
