package com.pluto.plugins.rooms.db.internal.ui.filter

import com.pluto.plugins.rooms.db.internal.FilterRelation

internal class FilterQueryTransformer private constructor() {
    companion object {
        fun transform(value: String?, relation: FilterRelation): String {
            return when (relation) {
                FilterRelation.Equals -> transformEquals(value)
                FilterRelation.NotEquals -> transformNotEquals(value)
                FilterRelation.Like -> " LIKE '%$value%'"
                FilterRelation.GreaterThan -> " > $value"
                FilterRelation.GreaterThanOrEquals -> " >= $value"
                FilterRelation.LessThan -> " < $value"
                FilterRelation.LessThanOrEquals -> " <= $value"
                FilterRelation.Between -> transformBetween(value)
                FilterRelation.In -> transformIn(value)
            }
        }

        private fun transformEquals(value: String?): String {
            return value?.let {
                " = '$it'"
            } ?: run {
                " IS NULL"
            }
        }

        private fun transformNotEquals(value: String?): String {
            return value?.let {
                " != '$it'"
            } ?: run {
                " IS NOT NULL"
            }
        }

        private fun transformIn(value: String?): String {
            val stringBuilder = StringBuilder()
            val split = value?.split(",")
            if (!split.isNullOrEmpty()) {
                stringBuilder.append(" IN (")
                split.forEachIndexed { index, data ->
                    stringBuilder.append("'${data.trim()}'")
                    if (index < split.lastIndex) {
                        stringBuilder.append(",")
                    }
                }
                stringBuilder.append(")")
            }
            return stringBuilder.toString()
        }

        private fun transformBetween(value: String?): String {
            val stringBuilder = StringBuilder()
            val split = value?.split(",")
            if (!split.isNullOrEmpty()) {
                stringBuilder.append(" BETWEEN")
                stringBuilder.append(" ${split[0].trim()}")
                stringBuilder.append(" AND")
                stringBuilder.append(" ${split[1].trim()}")
            }
            return stringBuilder.toString()
        }
    }
}
