package com.pluto.plugins.exceptions.internal.persistence

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pluto.plugins.exceptions.internal.DeviceInfo
import com.pluto.plugins.exceptions.internal.ExceptionAllData
import com.pluto.utilities.list.ListItem
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
@TypeConverters(EntityConverters::class)
@Entity(tableName = "exceptions")
internal data class ExceptionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "data")
    val data: ExceptionAllData,
    @ColumnInfo(name = "device")
    val device: DeviceInfo
) : ListItem()
