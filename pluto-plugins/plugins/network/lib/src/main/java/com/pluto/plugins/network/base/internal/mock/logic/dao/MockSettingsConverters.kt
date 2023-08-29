package com.pluto.plugins.network.base.internal.mock.logic.dao

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class MockSettingsConverters {

    private var moshi = Moshi.Builder().build()
    private var moshiAdapter: JsonAdapter<MockData> = moshi.adapter(MockData::class.java)

    @TypeConverter
    fun stringToMockData(data: String?): MockData? {
        data?.let { return moshiAdapter.fromJson(data) }
        return null
    }

    @TypeConverter
    fun mockDataToString(data: MockData): String? {
        return moshiAdapter.toJson(data)
    }
}
