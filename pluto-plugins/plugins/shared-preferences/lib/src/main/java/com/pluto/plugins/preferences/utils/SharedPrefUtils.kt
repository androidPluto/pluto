package com.pluto.plugins.preferences.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.pluto.plugins.preferences.R
import com.pluto.plugins.preferences.ui.SharedPrefFile
import com.pluto.plugins.preferences.ui.SharedPrefKeyValuePair
import com.pluto.plugins.preferences.utils.SharedPrefUtils.Companion.DEFAULT
import com.pluto.plugins.preferences.utils.SharedPrefUtils.Companion.isPlutoPref
import com.pluto.utilities.DebugLog
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.File

internal class SharedPrefUtils(private val context: Context) {

    private val preferences: Preferences = Preferences(context)
    private val moshi: Moshi = Moshi.Builder().build()
    private val moshiAdapter: JsonAdapter<List<String>?> = moshi.adapter(Types.newParameterizedType(List::class.java, String::class.java))
    internal var selectedPreferenceFiles: List<SharedPrefFile> = arrayListOf()
        get() {
            return preferences.selectedPreferenceFiles?.let {
                moshiAdapter.fromJson(it)?.map { label -> context.getPrefFile(label) }
            } ?: run {
                selectedPreferenceFiles = context.getSharePreferencesFiles()
                arrayListOf<SharedPrefFile>().apply {
                    addAll(context.getSharePreferencesFiles())
                    selectedPreferenceFiles = this
                }
            }
        }
        set(value) {
            preferences.selectedPreferenceFiles = moshiAdapter.toJson(value.map { it.label.toString() })
            field = value
        }

    internal val allPreferenceFiles = context.getSharePreferencesFiles()

    fun get(): List<SharedPrefKeyValuePair> {
        val list = arrayListOf<SharedPrefKeyValuePair>()
        val prefFilesList = selectedPreferenceFiles
        prefFilesList.forEach {
            val data = context.getPrefKeyValueMap(it)
            list.addAll(data.second)
        }
        list.sortBy { it.key }
        return list
    }

    fun set(pair: SharedPrefKeyValuePair, value: Any) {
        val prefFile = context.getPrefFile(pair.prefLabel ?: DEFAULT)
        val editor = context.getPrefManager(prefFile).edit()
        when (value) {
            is Int -> editor.putInt(pair.key, value).apply()
            is Long -> editor.putLong(pair.key, value).apply()
            is Float -> editor.putFloat(pair.key, value).apply()
            is Boolean -> editor.putBoolean(pair.key, value).apply()
            else -> editor.putString(pair.key, value.toString()).apply()
        }
    }

    companion object {
        const val DEFAULT = "Default"
        fun isPlutoPref(it: String): Boolean {
            return it.startsWith("_pluto_pref", true)
        }
    }
}

private fun Context.getSharePreferencesFiles(): ArrayList<SharedPrefFile> {
    val prefsDir = File(applicationInfo?.dataDir, "shared_prefs")
    val list = arrayListOf<SharedPrefFile>()
    if (prefsDir.exists() && prefsDir.isDirectory) {
        prefsDir.list()?.forEach {
            if (!isPlutoPref(it)) {
                list.add(
                    if (it == "${packageName}_preferences.xml") {
//                        SharedPrefFile(DEFAULT, true)
                        SharedPrefFile(createSpan { append(italic(light(fontColor(DEFAULT, color(com.pluto.plugin.R.color.pluto___text_dark_60))))) }, true)
                    } else {
                        val label = it.replace(".xml", "", true)
                        SharedPrefFile(label, false)
                    }
                )
            }
        }
    }
    return list
}

private fun Context.getPrefManager(file: SharedPrefFile): SharedPreferences =
    if (file.isDefault) {
        PreferenceManager.getDefaultSharedPreferences(this)
    } else {
        getSharedPreferences(file.label.toString(), Context.MODE_PRIVATE)
    }

private fun Context.getPrefKeyValueMap(file: SharedPrefFile): Pair<CharSequence, List<SharedPrefKeyValuePair>> {
    val prefManager = getPrefManager(file)
    val list = prefManager.list(file.label.toString(), file.isDefault)
    return Pair(file.label, list)
}

private fun SharedPreferences.list(label: String, default: Boolean): List<SharedPrefKeyValuePair> {
    val list = arrayListOf<SharedPrefKeyValuePair>()
    all.toList().forEach {
        list.add(SharedPrefKeyValuePair(it.first, it.second, label, default))
    }
    return list
}

@Suppress("TooGenericExceptionCaught")
private fun Context.getPrefFile(label: String): SharedPrefFile {
    try {
        val prefFilesList = getSharePreferencesFiles()
        return prefFilesList.first { it.label == label }
    } catch (e: Exception) {
        DebugLog.e("preferences", "error while fetching pref file", e)
    }
    return SharedPrefFile(DEFAULT, isDefault = true)
}
