package com.pluto.plugins.network.internal.mock.logic

import android.content.Context
import com.pluto.plugins.network.internal.database.DatabaseManager
import com.pluto.plugins.network.internal.interceptor.logic.pruneQueryParams
import com.pluto.plugins.network.internal.mock.logic.dao.MockData
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsDao
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal object MockSettingsRepo {
    private var mockSettingsDao: MockSettingsDao? = null
    private var mockSettingsMap = linkedSetOf<MockSettingsEntity>()

    fun init(context: Context) {
        mockSettingsDao = DatabaseManager(context).db.mockSettingsDao()
        GlobalScope.launch {
            populateMap()
        }
    }

    private suspend fun populateMap() {
        mockSettingsDao?.fetchList()?.let {
            mockSettingsMap.clear()
            mockSettingsMap.addAll(it)
        }
    }

    fun get(url: String, method: String): String? {
        val mockData =
            mockSettingsMap.singleOrNull { p ->
                p.requestUrl == url.pruneQueryParams() && p.requestMethod.lowercase() == method.lowercase()
            }?.mockData
        return mockData?.url // todo url before returning, like wildcards, response_status & delay
    }

    fun fetch(url: String, method: String): MockSettingsEntity? {
        return mockSettingsMap.singleOrNull { p -> p.requestUrl == url && p.requestMethod.lowercase() == method.lowercase() }
    }

    fun fetchList(search: String = ""): List<MockSettingsEntity> {
        return if (search.trim().isEmpty()) {
            mockSettingsMap.toList()
        } else {
            mockSettingsMap.filter { p -> p.requestUrl.contains(search.trim()) }
        }
    }

    suspend fun update(requestUrl: String, requestMethod: String, mockData: MockData) {
        val data = MockSettingsEntity(
            requestUrl = requestUrl,
            requestMethod = requestMethod,
            mockData = mockData,
            timestamp = System.currentTimeMillis()
        )
        mockSettingsDao?.let {
            it.save(data)
            populateMap()
        }
    }

    suspend fun delete(url: String) {
        mockSettingsDao?.let {
            it.delete(url)
            populateMap()
        }
    }

    suspend fun deleteAll() {
        mockSettingsDao?.let {
            it.deleteAll()
            populateMap()
        }
    }
}
