package com.pluto.plugins.network.internal.mock.logic.dao

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pluto.utilities.list.ListItem
import com.squareup.moshi.JsonClass

@Keep
@TypeConverters(MockSettingsConverters::class)
@Entity(tableName = "network_mock", indices = [Index(value = ["request_url"], unique = true)])
data class MockSettingsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "request_url")
    val requestUrl: String,
    @ColumnInfo(name = "request_method")
    val requestMethod: String,
    @ColumnInfo(name = "mock_data")
    val mockData: MockData
) : ListItem() {
    override fun isSame(other: Any): Boolean {
        return other is MockSettingsEntity && other.requestUrl == requestUrl
    }
}

@Keep
@JsonClass(generateAdapter = true)
data class MockData(
    val url: String,
    val statusCode: Int? = null,
    val delay: Int? = null
)
