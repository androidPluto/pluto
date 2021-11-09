package com.mocklets.pluto.network.internal.proxy.dao

import androidx.room.TypeConverter
import com.google.gson.Gson

internal class NetworkProxyConverters {

    private val gson = Gson()

    @TypeConverter
    fun stringToProxyData(data: String?): ProxyData {
        return gson.fromJson(data, ProxyData::class.java)
    }

    @TypeConverter
    fun proxyDataToString(data: ProxyData): String? {
        return gson.toJson(data)
    }
}
