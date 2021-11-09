package com.mocklets.pluto.network.internal.database

import androidx.room.RoomDatabase
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyDao
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyEntity

@androidx.room.Database(
    entities = [
        NetworkProxyEntity::class
    ],
    version = 2,
    exportSchema = false
)
internal abstract class PlutoNetworkDatabase : RoomDatabase() {
    abstract fun networkProxyDao(): NetworkProxyDao
}
