package com.pluto.plugins.rooms.db.internal

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.list.ListItem
import kotlinx.parcelize.Parcelize

@Keep
internal data class DatabaseModel(
    val name: String,
    val dbClass: Class<out RoomDatabase>
) : ListItem()

@Keep
internal data class TableModel(
    val name: String,
    val isSystemTable: Boolean
) : ListItem()

@Keep
internal data class ValuesModel(
    val index: Int,
    val columns: ArrayList<ColumnModel>,
    val values: ArrayList<String>?
) : ListItem()

/**
 * column properties (ordered)
 * cid, name, type, notnull, dflt_value, pk
 */

@Keep
@Parcelize
internal data class ColumnModel(
    val columnId: Int,
    val name: String,
    val type: String,
    val isNotNull: Boolean,
    val defaultValue: String?,
    val isPrimaryKey: Boolean
) : Parcelable

internal typealias RawTableContents = Pair<List<String>, List<List<String>>>

internal typealias ProcessedTableContents = Pair<List<ColumnModel>, List<List<String>>>

@Keep
internal data class EditEventData(
    val index: Int,
    val columns: List<ColumnModel>,
    val values: List<String>?
)
