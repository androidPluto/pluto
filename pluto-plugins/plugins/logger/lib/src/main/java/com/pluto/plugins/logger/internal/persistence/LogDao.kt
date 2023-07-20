package com.pluto.plugins.logger.internal.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface LogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: LogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(entities: List<LogEntity>)

    @Query("SELECT * FROM logs where id is :id")
    suspend fun fetch(id: Int): LogEntity?

    @Query("SELECT * FROM logs order by timestamp DESC")
    suspend fun fetchAll(): List<LogEntity>?

    @Query("DELETE FROM logs where id is :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM logs")
    suspend fun deleteAll()
}
