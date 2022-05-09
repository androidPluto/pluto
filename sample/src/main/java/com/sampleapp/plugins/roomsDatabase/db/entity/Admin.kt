package com.sampleapp.plugins.roomsDatabase.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class Admin(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = null,
    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean,
    @ColumnInfo(name = "can_write")
    val canWrite: Boolean
)
