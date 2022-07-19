package com.pluto.plugins.exceptions.internal.dao.database

import androidx.room.RoomDatabase
import com.pluto.plugins.exceptions.internal.dao.ExceptionDao
import com.pluto.plugins.exceptions.internal.dao.ExceptionEntity

@androidx.room.Database(
    entities = [
        ExceptionEntity::class,
    ],
    version = 3,
    exportSchema = false
)
internal abstract class PlutoDatabase : RoomDatabase() {
    abstract fun exceptionDao(): ExceptionDao
}
