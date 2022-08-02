package com.pluto.plugins.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugins.preferences.ui.SharedPrefFile
import com.pluto.plugins.preferences.ui.SharedPrefKeyValuePair
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.File

internal class SharedPrefRepo private constructor() {

    companion object {
        private lateinit var preferences: Preferences
        private val moshi: Moshi = Moshi.Builder().build()
        private val moshiAdapter: JsonAdapter<HashSet<SharedPrefFile>?> = moshi.adapter(Types.newParameterizedType(Set::class.java, SharedPrefFile::class.java))
        private var selectedPreferenceFiles: HashSet<SharedPrefFile>? = null
            get() = preferences.selectedPreferenceFiles?.let { moshiAdapter.fromJson(it) }
            set(value) {
                preferences.selectedPreferenceFiles = moshiAdapter.toJson(value)
                field = value
            }

        fun init(context: Context) {
            preferences = Preferences(context)
        }

        fun get(context: Context): List<SharedPrefKeyValuePair> {
            val list = arrayListOf<SharedPrefKeyValuePair>()
            val prefFilesList = getSelectedPreferenceFiles(context)
            prefFilesList.forEach {
                val data = context.getPrefKeyValueMap(it)
                list.addAll(data.second)
            }
            list.sortBy { it.key }
            return list
        }

        fun set(context: Context, pair: SharedPrefKeyValuePair, value: Any) {
            val prefFile = context.getPrefFile(pair.prefLabel ?: Preferences.DEFAULT)
            val editor = context.getPrefManager(prefFile).edit()
            when (value) {
                is Int -> editor.putInt(pair.key, value).apply()
                is Long -> editor.putLong(pair.key, value).apply()
                is Float -> editor.putFloat(pair.key, value).apply()
                is Boolean -> editor.putBoolean(pair.key, value).apply()
                else -> editor.putString(pair.key, value.toString()).apply()
            }
        }

        fun getSelectedPreferenceFiles(context: Context): HashSet<SharedPrefFile> {
            if (selectedPreferenceFiles == null) {
                selectedPreferenceFiles = hashSetOf<SharedPrefFile>().apply {
                    addAll(context.getSharePreferencesFiles())
                }
            }
            return selectedPreferenceFiles as HashSet<SharedPrefFile>
        }

        fun updateSelectedPreferenceFile(file: SharedPrefFile) {
            val selectedFiles: HashSet<SharedPrefFile> = selectedPreferenceFiles ?: hashSetOf()
            if (!selectedFiles.add(file)) {
                selectedFiles.remove(file)
            }
            selectedPreferenceFiles = selectedFiles
        }

        fun deSelectAll() {
            selectedPreferenceFiles = hashSetOf()
        }
    }
}

internal fun Context.getSharePreferencesFiles(): ArrayList<SharedPrefFile> {
    val prefsDir = File(applicationInfo?.dataDir, "shared_prefs")
    val list = arrayListOf<SharedPrefFile>()
    if (prefsDir.exists() && prefsDir.isDirectory) {
        prefsDir.list()?.forEach {
            if (!Preferences.isPlutoPref(it)) {
                list.add(
                    if (it == "${packageName}_preferences.xml") {
                        SharedPrefFile(Preferences.DEFAULT, true)
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
        getSharedPreferences(file.label, Context.MODE_PRIVATE)
    }

private fun Context.getPrefKeyValueMap(file: SharedPrefFile): Pair<String, List<SharedPrefKeyValuePair>> {
    val prefManager = getPrefManager(file)
    val list = prefManager.list(file.label, file.isDefault)
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
        prefFilesList.forEach {
            if (it.label == label) {
                return it
            }
        }
    } catch (e: Exception) {
        DebugLog.e("preferences", "error while fetching pref file", e)
    }
    return SharedPrefFile(Preferences.DEFAULT, isDefault = true)
}
