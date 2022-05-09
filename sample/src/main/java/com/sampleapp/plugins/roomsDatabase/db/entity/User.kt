package com.sampleapp.plugins.roomsDatabase.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = null,
    val gender: String,
    @ColumnInfo(defaultValue = "21")
    val age: Int,
    val phoneNumber: String,
    val email: String,
    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean
)
