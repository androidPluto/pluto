package com.pluto.plugins.rooms.db.internal

import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.list.ListItem

internal data class DatabaseModel(
    val name: String,
    val dbClass: Class<out RoomDatabase>
) : ListItem()

internal data class TableModel(
    val name: String,
    val isSystemTable: Boolean
) : ListItem()
