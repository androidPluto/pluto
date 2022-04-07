package com.pluto.plugins.rooms.db.internal

import com.pluto.plugin.utilities.list.ListItem

internal data class DatabaseModel(
    val dbName: String,
    val dbClass: Class<*>
) : ListItem()
