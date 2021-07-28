package com.mocklets.pluto.modules.exceptions.dao

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.exceptions.ExceptionAllData

@Keep
@TypeConverters(EntityConverters::class)
@Entity(tableName = "exceptions")
internal data class ExceptionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "data")
    val data: ExceptionAllData
) : ListItem()
