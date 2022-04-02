package com.pluto.plugins.exceptions.internal.dao

import androidx.room.TypeConverter
import com.pluto.plugins.exceptions.internal.ExceptionAllData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class EntityConverters {

    private var moshi = Moshi.Builder().build()
    private var moshiAdapter: JsonAdapter<ExceptionAllData> = moshi.adapter(ExceptionAllData::class.java)

    @TypeConverter
    fun stringToException(data: String?): ExceptionAllData? {
        data?.let { return moshiAdapter.fromJson(data) }
        return null
    }

    @TypeConverter
    fun exceptionToString(data: ExceptionAllData): String {
        return moshiAdapter.toJson(data)
    }
}
