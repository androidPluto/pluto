package com.pluto.plugins.logger.internal.persistence

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pluto.plugins.logger.internal.LogData
import com.pluto.plugins.logger.internal.Session
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
@TypeConverters(EntityConverters::class)
@Entity(tableName = "logs")
internal data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "session_id")
    val sessionId: String = Session.id,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "data")
    val data: LogData,
)
