package com.pluto.plugins.rooms.db.internal.core.query

import com.pluto.plugin.utilities.extensions.forEachIndexed

internal class Query private constructor() {

    class Database private constructor() {
        companion object {
            /**
             * Query to get all table names in the database.
             */
            const val GET_TABLE_NAMES = "SELECT name _id FROM sqlite_master WHERE type ='table'"
        }
    }

    class Tables private constructor() {
        companion object {
            /**
             * Query to get all column names of the table.
             *
             * @param table name of the table
             * @return query
             */
            fun getColumnNames(table: String) = "PRAGMA table_info($table)"

            /**
             * Query to get all values of the table.
             *
             * @param table name of the table
             * @return query
             */
            fun getAllValues(table: String) = "SELECT * FROM $table"

            /**
             * Query to drop the table.
             *
             * @param table name of the table
             * @return query
             */
            fun dropTable(table: String) = "DROP TABLE $table"

            /**
             * Query to clear all values of the table.
             *
             * @param table name of the table
             * @return query
             */
            fun deleteTable(table: String) = "DELETE FROM $table"
        }
    }

    class Rows private constructor() {
        companion object {
            /**
             * Query to insert values in to the table
             *
             * @param table name of the table
             * @param values list of values to be inserted
             * @return query
             */
            fun insertRow(table: String, values: List<String>): String {
                var insertQuery = "INSERT INTO $table VALUES("
                values.forEachIndexed { index, value ->
                    insertQuery += "'$value'"
                    if (index != values.size - 1) {
                        insertQuery += ","
                    }
                }
                insertQuery += ")"
                return insertQuery
            }

            /**
             * Query to update values of the table.
             *
             * @param table name of the table
             * @param column list of column names
             * @param oldValues list of current values
             * @param newValues list of values to be updated
             * @return query
             */
            @SuppressWarnings("StringLiteralDuplication")
            fun updateRow(table: String, column: List<String>, oldValues: List<String>, newValues: List<String>): String {
                var query = "Update $table set "
                Pair(column, newValues).forEachIndexed { index, columnName, value ->
                    query += "$columnName = '$value'"
                    if (index != column.size - 1) {
                        query += ", "
                    }
                }
                query += " where "
                Pair(column, oldValues).forEachIndexed { index, columnName, value ->
                    query += "$columnName = '$value'"
                    if (index != column.size - 1) {
                        query += " AND "
                    }
                }
                return query
            }

            /**
             * Query to delete values of a row.
             *
             * @param table name of the table
             * @param column list of column names
             * @param values list of values to be deleted
             * @return query
             */
            fun deleteRow(table: String, column: List<String>, values: List<String>): String {
                var query = "DELETE FROM $table where "
                Pair(column, values).forEachIndexed { index, columnName, value ->
                    query += "$columnName = '$value'"
                    if (index != column.size - 1) {
                        query += " AND "
                    }
                }
                return query
            }
        }
    }
}
