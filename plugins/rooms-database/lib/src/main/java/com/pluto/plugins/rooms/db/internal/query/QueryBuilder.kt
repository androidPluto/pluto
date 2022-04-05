package com.pluto.plugins.rooms.db.internal.query

import com.pluto.plugins.rooms.db.internal.forEachIndexed

object QueryBuilder {
    /**
     * Query to get all table names in the database.
     */
    const val GET_TABLE_NAMES = "SELECT name _id FROM sqlite_master WHERE type ='table'"

    /**
     * Query to get all column names of the table.
     *
     * @param tableName name of the table
     * @return query
     */
    infix fun getColumnNames(tableName: String) = "PRAGMA table_info($tableName)"

    /**
     * Query to get all values of the table.
     *
     * @param tableName name of the table
     * @return query
     */
    infix fun getAllValues(tableName: String) = "SELECT * FROM $tableName"

    /**
     * Query to drop the table.
     *
     * @param tableName name of the table
     * @return query
     */
    infix fun dropTable(tableName: String) = "DROP TABLE $tableName"

    /**
     * Query to clear all values of the table.
     *
     * @param tableName name of the table
     * @return query
     */
    infix fun deleteTable(tableName: String) = "DELETE FROM $tableName"

    /**
     * Query to insert values in to the table
     *
     * @param tableName name of the table
     * @param values list of values to be inserted
     * @return query
     */
    fun insert(tableName: String, values: List<String>): String {
        var insertQuery = "INSERT INTO $tableName VALUES("
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
     * @param tableName name of the table
     * @param columnNames list of column names
     * @param oldValues list of current values
     * @param newValues list of values to be updated
     * @return query
     */
    @SuppressWarnings("StringLiteralDuplication")
    fun updateTable(
        tableName: String,
        columnNames: List<String>,
        oldValues: List<String>,
        newValues: List<String>
    ): String {
        var query = "Update $tableName set "
        Pair(columnNames, newValues).forEachIndexed { index, columnName, value ->
            query += "$columnName = '$value'"
            if (index != columnNames.size - 1) {
                query += ", "
            }
        }
        query += " where "
        Pair(columnNames, oldValues).forEachIndexed { index, columnName, value ->
            query += "$columnName = '$value'"
            if (index != columnNames.size - 1) {
                query += " AND "
            }
        }
        return query
    }

    /**
     * Query to delete values of a row.
     *
     * @param tableName name of the table
     * @param columnNames list of column names
     * @param values list of values to be deleted
     * @return query
     */
    fun deleteRow(
        tableName: String,
        columnNames: List<String>,
        values: List<String>
    ): String {
        var query = "DELETE FROM $tableName where "
        Pair(columnNames, values).forEachIndexed { index, columnName, value ->
            query += "$columnName = '$value'"
            if (index != columnNames.size - 1) {
                query += " AND "
            }
        }
        return query
    }
}
