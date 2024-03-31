package com.pluto.plugins.datastore.pref.utils

import android.content.Context
import android.util.Log
import com.pluto.plugins.datastore.pref.PlutoDatastoreWatcher
import com.pluto.plugins.datastore.pref.PreferenceHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.views.keyvalue.KeyValuePairEditMetaData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class DatastorePrefUtils(context: Context) {

    private val preferences: Preferences = Preferences(context)
    private val moshi: Moshi = Moshi.Builder().build()
    private val moshiAdapter: JsonAdapter<List<String>?> = moshi.adapter(Types.newParameterizedType(List::class.java, String::class.java))

    internal var selectedPreferenceFiles: List<PreferenceHolder> = arrayListOf()
        get() {
            return preferences.selectedPreferenceFiles?.let {
                moshiAdapter.fromJson(it)?.map { label -> PlutoDatastoreWatcher.getSource(label) }
            } ?: run {
                selectedPreferenceFiles = allPreferenceFiles
                allPreferenceFiles
            }
        }
        set(value) {
            preferences.selectedPreferenceFiles = moshiAdapter.toJson(value.map { it.name })
            field = value
        }

    fun get(): List<DatastorePrefKeyValuePair> {
        return emptyList()
    }

    fun set(pair: DatastorePrefKeyValuePair, data: Any) {
        Log.d("", "$pair, $data")
    }

    val allPreferenceFiles: List<PreferenceHolder> = PlutoDatastoreWatcher.sources.value.toList()
}

internal data class DatastorePrefKeyValuePair(
    val key: String,
    val value: Any?,
    val prefLabel: String?,
    val isDefault: Boolean = false
) : ListItem(), KeyValuePairEditMetaData
