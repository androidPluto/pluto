package com.pluto.plugins.rooms.db.internal.core

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
//            filterConfigsMap[key]?.let { addAll(it) }
            addAll(configs)
        }
        filterConfigsMap[key] = list
    }

    fun add(databaseName: String, name: String, config: FilterModel) {
        val key = generateKey(databaseName, name)
        val list = arrayListOf<FilterModel>().apply {
            filterConfigsMap[key]?.let { addAll(it) }
            add(config)
        }
        filterConfigsMap[key] = list
    }

    private fun generateKey(databaseName: String, name: String) = "$databaseName::$name"
}
