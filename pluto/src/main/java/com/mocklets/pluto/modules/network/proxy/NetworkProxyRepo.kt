package com.mocklets.pluto.modules.network.proxy

import android.content.Context
import com.mocklets.pluto.core.database.DatabaseManager
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyDao
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.modules.network.proxy.dao.ProxyData
import com.mocklets.pluto.modules.network.pruneQueryParams
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

internal object NetworkProxyRepo {
    private var networkProxyDao: NetworkProxyDao? = null
    private var proxyMap = linkedSetOf<NetworkProxyEntity>()

    fun init(context: Context) {
        networkProxyDao = DatabaseManager(context).db.networkProxyDao()
        GlobalScope.launch {
            populateMap()
        }
    }

    private suspend fun populateMap() {
        networkProxyDao?.fetchList()?.let {
            proxyMap.clear()
            proxyMap.addAll(it)
        }
    }

    fun get(url: HttpUrl, method: String): String? {
        val proxyData =
            proxyMap.singleOrNull { p ->
                p.requestUrl == url.toString().pruneQueryParams() && p.requestMethod == method
            }?.proxyData
        return proxyData?.url // todo url before returning, like wildcards, response_status & delay
    }

    fun fetch(url: String, method: String): NetworkProxyEntity? {
        return proxyMap.singleOrNull { p -> p.requestUrl == url && p.requestMethod == method }
    }

    fun fetchList(search: String = ""): List<NetworkProxyEntity> {
        return if (search.trim().isEmpty()) {
            proxyMap.toList()
        } else {
            proxyMap.filter { p -> p.requestUrl.contains(search.trim()) }
        }
    }

    suspend fun update(requestUrl: String, requestMethod: String, proxyData: ProxyData) {
        val data = NetworkProxyEntity(
            requestUrl = requestUrl,
            requestMethod = requestMethod,
            proxyData = proxyData,
            timestamp = System.currentTimeMillis()
        )
        networkProxyDao?.let {
            it.save(data)
            populateMap()
        }
    }

    suspend fun delete(url: String) {
        networkProxyDao?.let {
            it.delete(url)
            populateMap()
        }
    }

    suspend fun deleteAll() {
        networkProxyDao?.let {
            it.deleteAll()
            populateMap()
        }
    }
}
