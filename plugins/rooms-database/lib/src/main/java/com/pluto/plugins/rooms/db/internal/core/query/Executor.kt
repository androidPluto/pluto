package com.pluto.plugins.rooms.db.internal.core.query

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

private typealias RowsAndColumns = Pair<List<String>, List<List<String>>>

/**
 * A class which is responsible for performing db operations.
 *
 */
internal class Executor private constructor(private val database: SupportSQLiteDatabase) {

    companion object {
        val instance: Executor
            get() = returnInstance()

        private fun returnInstance(): Executor {
            _instance?.let {
                return it
            }
            throw IllegalStateException("session not initialised")
        }

        private var _instance: Executor? = null

        /**
         * initialization function for [Executor].
         * Initializes [database] by using provided [databaseClass] and [databaseName].
         *
         * @param context [Context] of the accessing class
         * @param databaseClass a subclass of [RoomDatabase] registered in [Room] with @Database annotation
         * @param databaseName name of [RoomDatabase] class
         */
        @Synchronized
        fun initSession(context: Context, databaseName: String, databaseClass: Class<out RoomDatabase>): Executor {
            _instance?.let {
                throw IllegalStateException("session already initialised")
            } ?: run {
                val roomDatabase = Room.databaseBuilder(context, databaseClass, databaseName).build()
                _instance = Executor(roomDatabase.openHelper.writableDatabase)
                return _instance as Executor
            }
        }

        fun destroySession() {
            _instance = null
        }
    }

    /**
     * Query the db with given [query] and returns result in [onSuccess] or error in [onError].
     *
     * @param query SQL query
     * @param onSuccess action to be executed if [query] returns result
     * @param onError action to be executed if [query] execution failed
     */
    @SuppressWarnings("TooGenericExceptionCaught", "NestedBlockDepth")
    internal fun query(query: String, onSuccess: (RowsAndColumns) -> Unit, onError: (Exception) -> Unit) = try {
        val c = database.query(query, null)
        if (null == c) {
            onError(java.lang.Exception())
        } else {
            val columnNames = arrayListOf<String>()
            for (i in 0 until c.columnCount) columnNames.add(c.getColumnName(i))
            val rows = mutableListOf<ArrayList<String>>()
            c.moveToFirst()
            do {
                val rowValues = arrayListOf<String>()
                for (i in 0 until c.columnCount) {
                    try {
                        val value = c.getString(i)
                        rowValues.add(value)
                    } catch (e: Exception) {
                        onError(e)
                    }
                }
                if (rowValues.isNotEmpty()) {
                    rows.add(rowValues)
                }
            } while (c.moveToNext())
            c.close()
            onSuccess(
                RowsAndColumns(
                    columnNames,
                    rows
                )
            )
        }
    } catch (ex: Exception) {
        onError(ex)
    }

    /**
     * Executes the given [query].
     *
     * @param query SQL query
     * @param onSuccess action to be executed if [query] executed successfully
     * @param onError action to be executed if [query] execution failed
     */
    @SuppressWarnings("TooGenericExceptionCaught")
    internal fun execute(query: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) = try {
        database.execSQL(query).also { onSuccess() }
    } catch (ex: Exception) {
        onError(ex)
    }
}
