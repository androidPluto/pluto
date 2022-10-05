package com.pluto.plugins.rooms.db.internal.core

internal fun isSystemTable(name: String): Boolean {
    val systemTables = arrayListOf<String>().apply {
        add("android_metadata")
        add("room_master_table")
        add("sqlite_master")
        add("sqlite_sequence")
    }
    return systemTables.contains(name)
}
