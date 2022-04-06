package com.pluto.plugins.rooms.db

import androidx.room.RoomDatabase
import com.pluto.plugins.rooms.db.internal.DatabaseModel

object PlutoRoomsDB {

    internal val watchList = arrayListOf<DatabaseModel>()

    fun watch(dbClass: Class<out RoomDatabase>, dbName: String) {
        watchList.add(DatabaseModel(dbClass, dbName))
    }
}
