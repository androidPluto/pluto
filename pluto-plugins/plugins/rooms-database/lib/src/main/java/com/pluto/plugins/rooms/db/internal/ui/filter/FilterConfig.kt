package com.pluto.plugins.rooms.db.internal.ui.filter

import com.pluto.plugins.rooms.db.internal.FilterModel

internal object FilterConfig {
    private val filterConfigsMap: HashMap<String, List<FilterModel>?> = hashMapOf()

    fun get(databaseName: String, name: String): List<FilterModel> {
        return filterConfigsMap[generateKey(databaseName, name)] ?: emptyList()
    }

    fun delete(databaseName: String, name: String) {
        filterConfigsMap.remove(generateKey(databaseName, name))
    }

    fun set(databaseName: String, name: String, configs: List<FilterModel>) {
        val key = generateKey(databaseName, name)
        val list = arrayListOf<FilterModel>().apply {
            addAll(configs)
        }
        filterConfigsMap[key] = list
    }

    private fun generateKey(databaseName: String, name: String) = "$databaseName::$name"
    fun clear() {
        filterConfigsMap.clear()
    }
}
