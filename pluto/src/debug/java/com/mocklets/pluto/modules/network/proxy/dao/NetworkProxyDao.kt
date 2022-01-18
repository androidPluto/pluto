package com.mocklets.pluto.modules.network.proxy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface NetworkProxyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: NetworkProxyEntity)

    @Query("SELECT * FROM network_proxy where request_url is :requestUrl")
    suspend fun fetch(requestUrl: String): NetworkProxyEntity?

    @Query("SELECT * FROM network_proxy where request_url like '%' || :search || '%' order by timestamp DESC")
    suspend fun fetchList(search: String = ""): List<NetworkProxyEntity>?

    @Query("DELETE FROM network_proxy where request_url is :requestUrl")
    suspend fun delete(requestUrl: String)

    @Query("DELETE FROM network_proxy")
    suspend fun deleteAll()
}
