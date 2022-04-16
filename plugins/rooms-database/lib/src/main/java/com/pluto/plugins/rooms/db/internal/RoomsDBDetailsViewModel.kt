package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import android.widget.HorizontalScrollView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.SingleLiveEvent
import com.pluto.plugins.rooms.db.internal.core.TableGridView
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.Query
import com.pluto.plugins.rooms.db.internal.core.query.QueryExecutor
import java.lang.Exception
import kotlinx.coroutines.launch

internal class RoomsDBDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val tables: LiveData<Pair<List<TableModel>, Exception?>>
        get() = _tables
    private val _tables = MutableLiveData<Pair<List<TableModel>, Exception?>>()

    val currentTable: LiveData<TableModel?>
        get() = _currentTable
    private val _currentTable = MutableLiveData<TableModel?>()

    val dataView: LiveData<Pair<HorizontalScrollView?, Exception?>>
        get() = _dataView
    private val _dataView = SingleLiveEvent<Pair<HorizontalScrollView?, Exception?>>()

    val addRecordEvent: LiveData<Pair<EditEventData?, Exception?>>
        get() = _addRecordEvent
    private val _addRecordEvent = SingleLiveEvent<Pair<EditEventData?, Exception?>>()

    fun init(context: Context, name: String, dbClass: Class<out RoomDatabase>) {
        QueryExecutor.init(context, name, dbClass)
        fetchTables()
    }

    private fun fetchTables() {
        viewModelScope.launch {
            val tables = arrayListOf<String>()
            QueryExecutor.query(
                Query.Database.GET_TABLE_NAMES,
                {
                    it.second.forEach { list ->
                        tables.addAll(list)
                    }

                    val processedTableList = arrayListOf<TableModel>()
                    val processedSystemTableList = arrayListOf<TableModel>()
                    tables.forEach { table ->
                        if (isSystemTable(table)) {
                            processedSystemTableList.add(TableModel(table, true))
                        } else {
                            processedTableList.add(TableModel(table, false))
                        }
                    }
                    if (processedTableList.size == 1) {
                        _currentTable.postValue(processedTableList.first())
                    } else {
                        _currentTable.postValue(null)
                    }
                    _tables.postValue(Pair(processedTableList.plus(processedSystemTableList), null))
                },
                {
                    _tables.postValue(Pair(emptyList(), it))
                }
            )
        }
    }

    fun selectTable(table: TableModel) {
        _currentTable.postValue(table)
    }

    fun fetchData(context: Context, table: String, onClick: (Int, List<String>, List<String>) -> Unit) {
        val hsv = HorizontalScrollView(context)
        viewModelScope.launch {
            QueryExecutor.query(
                Query.Tables.getAllValues(table),
                { result ->
                    val columns = result.first
                    val rows = result.second
                    TableGridView(context).create(columns, rows) {
                        onClick(it, columns, rows[it])
                    }.also { hsv.addView(it) }
                    _dataView.postValue(Pair(hsv, null))
                },
                { ex ->
                    _dataView.postValue(Pair(null, ex))
                }
            )
        }
    }

    fun triggerAddRecordEvent(table: String, index: Int, list: List<String>?) {
        viewModelScope.launch {
            QueryExecutor.query(
                Query.Tables.getColumnNames(table),
                {
                    result ->
                    run {
                        val eventData = EditEventData(index = index, columns = result.second.map { it[1] }, values = list)
                        _addRecordEvent.postValue(Pair(eventData, null))
                    }
                },
                {
                    ex ->
                    _addRecordEvent.postValue(Pair(null, ex))
                }
            )
        }
    }
}
