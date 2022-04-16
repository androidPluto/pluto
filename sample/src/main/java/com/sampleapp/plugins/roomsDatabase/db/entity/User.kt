package com.sampleapp.plugins.roomsDatabase.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val gender: String,
    @ColumnInfo(defaultValue = "21")
    val age: Int,
    val phoneNumber: String,
    val email: String,
    val column1: String? = null,
    @ColumnInfo(defaultValue = "column2")
    val column2: String = "column2",
    val column3: Int = 0
)
