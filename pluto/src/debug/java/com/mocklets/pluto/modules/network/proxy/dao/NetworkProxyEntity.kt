package com.mocklets.pluto.modules.network.proxy.dao

import androidx.annotation.Keep
import androidx.room.*
import com.mocklets.pluto.core.ui.list.ListItem

@Keep
@TypeConverters(NetworkProxyConverters::class)
@Entity(tableName = "network_proxy", indices = [Index(value = ["request_url"], unique = true)])
internal data class NetworkProxyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "request_url")
    val requestUrl: String,
    @ColumnInfo(name = "request_method")
    val requestMethod: String,
    @ColumnInfo(name = "proxy_data")
    val proxyData: ProxyData
) : ListItem() {
    override fun isSame(other: Any): Boolean {
        return other is NetworkProxyEntity && other.requestUrl == requestUrl
    }

    override fun equals(other: Any?): Boolean {
        return other is NetworkProxyEntity && other.requestUrl == requestUrl
    }
}

@Keep
internal data class ProxyData(
    val url: String,
    val statusCode: Int? = null,
    val delay: Int? = null
)
