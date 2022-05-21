package com.sampleapp.plugins.roomsDatabase.db2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sampleapp.plugins.roomsDatabase.db2.entity.UserV2

@Dao
internal interface UserV2DAO {

    @Insert
    fun insert(user: UserV2)

    @Query("DELETE FROM UserV2")
    fun clear()
}
