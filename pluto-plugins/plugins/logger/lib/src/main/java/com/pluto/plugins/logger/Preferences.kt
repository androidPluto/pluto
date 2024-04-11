package com.pluto.plugins.logger

import android.content.Context
import com.pluto.plugins.logger.internal.LogTimeStamp
import com.pluto.plugins.logger.internal.LogType
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class Preferences(context: Context) {

    private val settingsPrefs by lazy { context.preferences("_pluto_log_filter_settings") }
    private val moshi: Moshi = Moshi.Builder().build()
    private val moshiAdapter: JsonAdapter<List<String>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, String::class.java))

    private var timeStampAdapter: JsonAdapter<LogTimeStamp> =
        moshi.adapter(LogTimeStamp::class.java)

    internal var selectedFilterLogType: List<LogType>
        get() = settingsPrefs.getString(SELECTED_FILTER_LOG_TYPE, null)?.let {
            moshiAdapter.fromJson(it)?.map { type -> LogType(type) }
        } ?: run { emptyList() }
        set(value) = settingsPrefs.edit()
            .putString(SELECTED_FILTER_LOG_TYPE, moshiAdapter.toJson(value.map { it.type })).apply()

    internal var selectedFilterTime: LogTimeStamp
        get() = settingsPrefs.getString(SELECTED_FILTER_TIMESTAMP, null)?.let {
            timeStampAdapter.fromJson(it)
        } ?: run { LogTimeStamp(0, false) }
        set(value) = settingsPrefs.edit()
            .putString(SELECTED_FILTER_TIMESTAMP, timeStampAdapter.toJson(value)).apply()

    companion object {
        private const val SELECTED_FILTER_LOG_TYPE = "selected_filter_logtype"
        private const val SELECTED_FILTER_TIMESTAMP = "selected_filter_timestamp"
    }

    fun clearFilters() {
        settingsPrefs.edit().clear().apply()
    }
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) =
    getSharedPreferences(name, mode)
