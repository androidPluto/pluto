package com.mocklets.pluto.core.database

import androidx.room.RoomDatabase
import com.mocklets.pluto.modules.exceptions.dao.ExceptionDao
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyDao
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyEntity

@androidx.room.Database(
    entities = [
        ExceptionEntity::class,
        NetworkProxyEntity::class
    ],
    version = 2
)
internal abstract class PlutoDatabase : RoomDatabase() {
    abstract fun exceptionDao(): ExceptionDao
    abstract fun networkProxyDao(): NetworkProxyDao
}
