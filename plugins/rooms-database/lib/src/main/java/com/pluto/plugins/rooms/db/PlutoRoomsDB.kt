package com.pluto.plugins.rooms.db

import com.pluto.plugins.rooms.db.internal.DatabaseModel

object PlutoRoomsDB {

    internal val watchList = arrayListOf<DatabaseModel>()

    fun watch(dbClass: Class<*>, dbName: String) {
        watchList.add(DatabaseModel(dbClass, dbName))
    }
}
