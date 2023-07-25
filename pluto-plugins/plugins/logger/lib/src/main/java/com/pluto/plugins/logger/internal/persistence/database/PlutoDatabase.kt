package com.pluto.plugins.logger.internal.persistence.database

import androidx.room.RoomDatabase
import com.pluto.plugins.logger.internal.persistence.LogDao
import com.pluto.plugins.logger.internal.persistence.LogEntity

@androidx.room.Database(
    entities = [
        LogEntity::class,
    ],
    version = 2,
    exportSchema = false
)
internal abstract class PlutoDatabase : RoomDatabase() {
    abstract fun exceptionDao(): LogDao
}
