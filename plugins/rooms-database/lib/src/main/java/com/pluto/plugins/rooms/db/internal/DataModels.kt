package com.pluto.plugins.rooms.db.internal

import androidx.annotation.Keep
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.list.ListItem

@Keep
internal data class DatabaseModel(
    val name: String,
    val dbClass: Class<out RoomDatabase>
) : ListItem()

@Keep
internal data class TableModel(
    val name: String,
    val isSystemTable: Boolean
) : ListItem()

@Keep
internal data class ValuesModel(
    val index: Int,
    val columns: ArrayList<String>,
    val values: ArrayList<String>?
) : ListItem()
