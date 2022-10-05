package com.pluto.plugins.exceptions.internal.persistence

import androidx.room.TypeConverter
import com.pluto.plugins.exceptions.internal.DeviceInfo
import com.pluto.plugins.exceptions.internal.ExceptionAllData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class EntityConverters {

    private var moshi = Moshi.Builder().build()
    private var exceptionMoshiAdapter: JsonAdapter<ExceptionAllData> = moshi.adapter(ExceptionAllData::class.java)
    private var deviceMoshiAdapter: JsonAdapter<DeviceInfo> = moshi.adapter(DeviceInfo::class.java)

    @TypeConverter
    fun stringToException(data: String?): ExceptionAllData? {
        data?.let { return exceptionMoshiAdapter.fromJson(data) }
        return null
    }

    @TypeConverter
    fun exceptionToString(data: ExceptionAllData): String {
        return exceptionMoshiAdapter.toJson(data)
    }

    @TypeConverter
    fun stringToDevice(data: String?): DeviceInfo? {
        data?.let { return deviceMoshiAdapter.fromJson(data) }
        return null
    }

    @TypeConverter
    fun deviceToString(data: DeviceInfo): String {
        return deviceMoshiAdapter.toJson(data)
    }
}
