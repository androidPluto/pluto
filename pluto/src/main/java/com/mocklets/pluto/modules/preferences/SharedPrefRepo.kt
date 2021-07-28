package com.mocklets.pluto.modules.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.core.preferences.Preferences
import com.mocklets.pluto.modules.preferences.ui.SharedPrefFile
import com.mocklets.pluto.modules.preferences.ui.SharedPrefKeyValuePair
import java.io.File
import java.lang.reflect.Type

internal object SharedPrefRepo {

//    private val prefList: ArrayList<SharedPrefFile>
//        get() = Pluto.appContext.getSharePreferencesFiles()

    private val preferences by lazy { Pluto.preferences }
    private val selectedFileListType: Type = object : TypeToken<HashSet<SharedPrefFile>>() {}.type

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

    internal fun getSelectedPreferenceFiles(context: Context): Set<SharedPrefFile> {
        var selectedFiles: HashSet<SharedPrefFile>? =
            Gson().fromJson(preferences.selectedPreferenceFiles, selectedFileListType)
        if (selectedFiles == null) {
            preferences.selectedPreferenceFiles = Gson().toJson(context.getSharePreferencesFiles())
            selectedFiles = Gson().fromJson(preferences.selectedPreferenceFiles, selectedFileListType)
        }
        return selectedFiles ?: emptySet()
    }

    fun updateSelectedPreferenceFile(file: SharedPrefFile) {
        val selectedFiles: HashSet<SharedPrefFile>? =
            Gson().fromJson(preferences.selectedPreferenceFiles, selectedFileListType)
        selectedFiles?.let {
            if (!it.add(file)) {
                it.remove(file)
            }
            preferences.selectedPreferenceFiles = Gson().toJson(selectedFiles)
        }
    }

    fun deSelectAll() {
        preferences.selectedPreferenceFiles = Gson().toJson(emptySet<SharedPrefFile>())
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
