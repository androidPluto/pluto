package com.sampleapp.plugins.roomsDatabase.db2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sampleapp.plugins.roomsDatabase.db2.dao.UserV2DAO
import com.sampleapp.plugins.roomsDatabase.db2.entity.UserV2

@Database(entities = [UserV2::class], version = 5)
abstract class Sample2Database : RoomDatabase() {

    internal abstract fun userDao(): UserV2DAO

    companion object {
        const val DB_NAME = "sample_2_database"
        private val lock = Any()
        private var INSTANCE: Sample2Database? = null

        fun getInstance(context: Context): Sample2Database {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = createDB(context)
                }
                return INSTANCE as Sample2Database
            }
        }

        private fun createDB(context: Context): Sample2Database {
            val database: Builder<Sample2Database> =
                Room.databaseBuilder(context, Sample2Database::class.java, DB_NAME)
            return database
                .addMigrations()
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}
