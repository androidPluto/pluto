package com.sampleapp.plugins.roomsDatabase.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sampleapp.plugins.roomsDatabase.db.dao.AdminDAO
import com.sampleapp.plugins.roomsDatabase.db.dao.UserDAO
import com.sampleapp.plugins.roomsDatabase.db.entity.Admin
import com.sampleapp.plugins.roomsDatabase.db.entity.User

@Database(entities = [User::class, Admin::class], version = 5)
abstract class SampleDatabase : RoomDatabase() {

    internal abstract fun userDao(): UserDAO
    internal abstract fun adminDao(): AdminDAO

    companion object {
        const val DB_NAME = "sample_database"
        private val lock = Any()
        private var INSTANCE: SampleDatabase? = null

        fun getInstance(context: Context): SampleDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = createDB(context)
                }
                return INSTANCE as SampleDatabase
            }
        }

        private fun createDB(context: Context): SampleDatabase {
            val database: Builder<SampleDatabase> =
                Room.databaseBuilder(context, SampleDatabase::class.java, DB_NAME)
            return database
                .addMigrations()
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}
