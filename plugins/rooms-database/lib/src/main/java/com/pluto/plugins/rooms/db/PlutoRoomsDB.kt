package com.pluto.plugins.rooms.db

import androidx.room.RoomDatabase
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object PlutoRoomsDB {

    internal val watchList = MutableStateFlow<Set<DatabaseModel>>(emptySet())

    fun watch(name: String, dbClass: Class<out RoomDatabase>) {
        watchList.update { oldSet ->
            mutableSetOf<DatabaseModel>().apply {
                addAll(oldSet)
                add(DatabaseModel(name, dbClass))
            }
        }
    }

    fun remove(name: String) {
        watchList.update { oldSet ->
            mutableSetOf<DatabaseModel>().apply {
                oldSet.forEach {
                    if (it.dbName != name) add(it)
                }
            }
        }
    }
}
