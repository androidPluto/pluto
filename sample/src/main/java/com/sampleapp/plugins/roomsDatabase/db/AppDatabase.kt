package com.sampleapp.plugins.roomsDatabase.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sampleapp.plugins.roomsDatabase.db.dao.UserDAO
import com.sampleapp.plugins.roomsDatabase.db.entity.User

@Database(entities = [User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    internal abstract fun personDAO(): UserDAO

    companion object {
        const val DB_NAME = "sample_database"
        private val lock = Any()
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = createDB(context)
                }
                return INSTANCE as AppDatabase
            }
        }

        private fun createDB(context: Context): AppDatabase {
            val database: Builder<AppDatabase> =
                Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            return database
                .addMigrations()
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}
