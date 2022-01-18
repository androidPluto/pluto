package com.mocklets.pluto.modules.exceptions.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mocklets.pluto.modules.exceptions.ExceptionAllData

internal class EntityConverters {

    private val gson = Gson()

    @TypeConverter
    fun stringToException(data: String?): ExceptionAllData {
        return gson.fromJson(data, ExceptionAllData::class.java)
    }

    @TypeConverter
    fun exceptionToString(data: ExceptionAllData): String? {
        return gson.toJson(data)
    }
}
