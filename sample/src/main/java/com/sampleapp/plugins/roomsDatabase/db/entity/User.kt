package com.sampleapp.plugins.roomsDatabase.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val gender: String,
    val age: Int,
    val phoneNumber: String,
    val email: String
)
