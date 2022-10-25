package com.pluto.plugins.network.internal.database

import androidx.room.RoomDatabase
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsDao
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity

@androidx.room.Database(
    entities = [
        MockSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class PlutoNetworkDatabase : RoomDatabase() {
    abstract fun mockSettingsDao(): MockSettingsDao
}
