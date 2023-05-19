package com.pluto.plugins.logger.internal.persistence.database

import android.content.Context
import androidx.room.Room

internal class DatabaseManager(context: Context) {

    val db by lazy {
        Room.databaseBuilder(context, PlutoDatabase::class.java, DATABASE_NAME)
            .addMigrations()
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        private const val DATABASE_NAME = "_pluto_log_database"
    }
}
