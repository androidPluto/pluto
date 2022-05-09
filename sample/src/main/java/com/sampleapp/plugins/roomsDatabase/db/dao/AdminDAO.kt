package com.sampleapp.plugins.roomsDatabase.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sampleapp.plugins.roomsDatabase.db.entity.Admin

@Dao
internal interface AdminDAO {

    @Insert
    fun insert(user: Admin)

    @Query("DELETE FROM Admin")
    fun clear()
}
