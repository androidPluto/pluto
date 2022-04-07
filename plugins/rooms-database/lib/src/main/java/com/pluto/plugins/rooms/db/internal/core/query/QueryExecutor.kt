package com.pluto.plugins.rooms.db.internal.core.query

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

typealias RowsAndColumns = Pair<List<String>, List<List<String>>>

/**
 * A class which is responsible for performing db operations.
 *
 */
object QueryExecutor {
    private lateinit var database: SupportSQLiteDatabase

    /**
     * initialization function for [QueryExecutor].
     * Initializes [database] by using provided [databaseClass] and [databaseName].
     *
     * @param context [Context] of the accessing class
     * @param databaseClass a subclass of [RoomDatabase] registered in [Room] with @Database annotation
     * @param databaseName name of [RoomDatabase] class
     */
    fun init(context: Context, databaseName: String, databaseClass: Class<out RoomDatabase>) {
        val roomDatabase = Room.databaseBuilder(context, databaseClass, databaseName).build()
        database = roomDatabase.openHelper.writableDatabase
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
