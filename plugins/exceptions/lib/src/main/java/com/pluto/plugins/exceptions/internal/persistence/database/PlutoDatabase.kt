package com.pluto.plugins.exceptions.internal.persistence.database

import androidx.room.RoomDatabase
import com.pluto.plugins.exceptions.internal.persistence.ExceptionDao
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity

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
