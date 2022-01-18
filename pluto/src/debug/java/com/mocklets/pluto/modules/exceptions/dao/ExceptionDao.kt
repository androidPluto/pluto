package com.mocklets.pluto.modules.exceptions.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface ExceptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: ExceptionEntity)

    @Query("SELECT * FROM exceptions where id is :id")
    suspend fun fetch(id: Int): ExceptionEntity?

    @Query("SELECT * FROM exceptions order by timestamp DESC")
    suspend fun fetchAll(): List<ExceptionEntity>?

    @Query("DELETE FROM exceptions where id is :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM exceptions")
    suspend fun deleteAll()
}
