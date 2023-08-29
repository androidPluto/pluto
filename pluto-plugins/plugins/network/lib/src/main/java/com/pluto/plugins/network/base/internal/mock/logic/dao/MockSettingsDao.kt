package com.pluto.plugins.network.base.internal.mock.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface MockSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: MockSettingsEntity)

    @Query("SELECT * FROM network_mock where request_url is :requestUrl")
    suspend fun fetch(requestUrl: String): MockSettingsEntity?

    @Query("SELECT * FROM network_mock where request_url like '%' || :search || '%' order by timestamp DESC")
    suspend fun fetchList(search: String = ""): List<MockSettingsEntity>?

    @Query("DELETE FROM network_mock where request_url is :requestUrl")
    suspend fun delete(requestUrl: String)

    @Query("DELETE FROM network_mock")
    suspend fun deleteAll()
}
