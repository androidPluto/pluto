package com.pluto.plugins.logger.internal.persistence

import androidx.room.TypeConverter
import com.pluto.plugins.logger.internal.LogData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class EntityConverters {

    private var moshi = Moshi.Builder().build()
    private var logMoshiAdapter: JsonAdapter<LogData> = moshi.adapter(LogData::class.java)

    @TypeConverter
    fun stringToLog(data: String?): LogData? {
        data?.let { return logMoshiAdapter.fromJson(data) }
        return null
    }

    @TypeConverter
    fun logToString(data: LogData): String {
        return logMoshiAdapter.toJson(data)
    }
}
