package com.pluto.plugins.network.base.internal.database

import android.content.Context
import androidx.room.Room

internal class DatabaseManager(context: Context) {

    val db by lazy {
        Room.databaseBuilder(context, PlutoNetworkDatabase::class.java, DATABASE_NAME)
            .addMigrations()
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        private const val DATABASE_NAME = "_pluto_network_database"
    }
}
