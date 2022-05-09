package com.sampleapp.plugins.roomsDatabase.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sampleapp.plugins.roomsDatabase.db.entity.User

@Dao
internal interface UserDAO {

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM User")
    fun clear()
}
