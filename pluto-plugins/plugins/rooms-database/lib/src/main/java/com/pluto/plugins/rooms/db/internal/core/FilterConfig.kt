package com.pluto.plugins.rooms.db.internal.core

import com.pluto.plugins.rooms.db.internal.FilterModel

internal object FilterConfig {
    private val filterConfigsMap: HashMap<String, List<FilterModel>?> = hashMapOf()

    fun get(databaseName: String, name: String): List<FilterModel> {
        return filterConfigsMap["$databaseName:$name"] ?: emptyList()
    }

    fun set(databaseName: String, name: String, configs: List<FilterModel>) {
        filterConfigsMap["$databaseName:$name"] = configs
    }
}
