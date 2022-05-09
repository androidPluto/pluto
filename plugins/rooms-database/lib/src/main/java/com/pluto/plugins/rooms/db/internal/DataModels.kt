package com.pluto.plugins.rooms.db.internal

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugins.rooms.db.R
import kotlinx.parcelize.Parcelize

@Keep
internal data class DatabaseModel(
    val name: String,
    val dbClass: Class<out RoomDatabase>
) : ListItem()

@Keep
@Parcelize
internal data class TableModel(
    val name: String,
    val isSystemTable: Boolean
) : Parcelable, ListItem()

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
) : Parcelable, ListItem()

internal typealias RawTableContents = Pair<List<String>, List<List<String>>>

internal typealias ProcessedTableContents = Pair<List<ColumnModel>, List<List<String>>>

@Keep
@Parcelize
internal data class RowDetailsData(
    val index: Int,
    val table: String,
    val columns: List<ColumnModel>,
    val values: List<String>?
) : Parcelable

internal data class FilterModel(
    val column: String,
    val value: String?,
    val relation: FilterRelation
)

internal sealed class RowAction {
    class Click(val isInsert: Boolean) : RowAction()
    object LongClick : RowAction()
    object Delete : RowAction()
    object Duplicate : RowAction()
}

internal sealed class FilterRelation {
    object Equals : FilterRelation()
    object Like : FilterRelation()
}

internal sealed class SortBy(val label: String, @DrawableRes val indicator: Int) {
    class Asc(label: String = "ASC", indicator: Int = R.drawable.pluto_rooms___ic_sort_indicator_asc) : SortBy(label, indicator)
    class Desc(label: String = "DESC", indicator: Int = R.drawable.pluto_rooms___ic_sort_indicator_desc) : SortBy(label, indicator)
}
