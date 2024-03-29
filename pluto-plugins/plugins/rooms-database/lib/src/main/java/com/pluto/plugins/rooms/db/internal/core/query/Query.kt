package com.pluto.plugins.rooms.db.internal.core.query

import com.pluto.plugins.rooms.db.internal.FilterModel
import com.pluto.plugins.rooms.db.internal.SortBy
import com.pluto.plugins.rooms.db.internal.ui.filter.FilterQueryTransformer
import java.lang.StringBuilder

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
            fun columns(table: String) = "PRAGMA table_info($table)"

            /**
             * Query to get all values of the table.
             *
             * @param table name of the table
             * @param filters list of filters
             * @param sortBy sorting logic
             * @return query
             */
            fun values(table: String, filters: List<FilterModel>?, sortBy: Pair<String, SortBy>?): String {
                val stringBuilder = StringBuilder()
                stringBuilder.append("SELECT * FROM $table")
                if (!filters.isNullOrEmpty()) {
                    stringBuilder.append(" WHERE")
                    filters.forEachIndexed { index, filter ->
                        stringBuilder.append(" ${filter.column.name}")
                        stringBuilder.append(FilterQueryTransformer.transform(filter.value, filter.relation))
                        if (index < filters.lastIndex) {
                            stringBuilder.append(" AND")
                        }
                    }
                }
                sortBy?.let {
                    stringBuilder.append(" ORDER BY ${sortBy.first} ${sortBy.second.label.uppercase()}")
                }
                return stringBuilder.toString()
            }

            /**
             * Query to count the rows in the table.
             *
             * @param table name of the table
             * @return query
             */
            fun count(table: String) = "SELECT COUNT(*) FROM $table"

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
            fun clear(table: String) = "DELETE FROM $table"
        }
    }
}
