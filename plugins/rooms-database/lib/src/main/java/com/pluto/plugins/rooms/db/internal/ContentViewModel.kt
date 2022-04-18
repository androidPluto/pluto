package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.SingleLiveEvent
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.Executor
import com.pluto.plugins.rooms.db.internal.core.query.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class ContentViewModel(application: Application) : AndroidViewModel(application) {

    val tables: LiveData<Pair<List<TableModel>, Exception?>>
        get() = _tables
    private val _tables = MutableLiveData<Pair<List<TableModel>, Exception?>>()

    val currentTable: LiveData<TableModel?>
        get() = _currentTable
    private val _currentTable = MutableLiveData<TableModel?>()

    val tableContent: LiveData<Pair<TableContents?, Exception?>>
        get() = _tableContent
    private val _tableContent = SingleLiveEvent<Pair<TableContents?, Exception?>>()

    val addRecordEvent: LiveData<Pair<EditEventData?, Exception?>>
        get() = _addRecordEvent
    private val _addRecordEvent = SingleLiveEvent<Pair<EditEventData?, Exception?>>()

    override fun onCleared() {
        super.onCleared()
        Executor.destroySession()
    }

    fun init(context: Context, name: String, dbClass: Class<out RoomDatabase>) {
        Executor.initSession(context, name, dbClass)
        fetchTables()
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    private fun fetchTables() {
        viewModelScope.launch(Dispatchers.IO) {
            val tables = arrayListOf<String>()
            try {
                val queryResult = Executor.instance.query(Query.Database.GET_TABLE_NAMES)
                queryResult.second.forEach { list ->
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
            } catch (e: Exception) {
                _tables.postValue(Pair(emptyList(), e))
            }
        }
    }

    fun selectTable(table: TableModel) {
        _currentTable.postValue(table)
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun fetchData(table: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val contents = Executor.instance.query(Query.Tables.getAllValues(table))
                _tableContent.postValue(Pair(contents, null))
            } catch (e: Exception) {
                _tableContent.postValue(Pair(null, e))
            }
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun triggerAddRecordEvent(table: String, index: Int, list: List<String>?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val queryResult = Executor.instance.query(Query.Tables.getColumnNames(table))
                val eventData = EditEventData(index = index, columns = queryResult.second.map { it[1] }, values = list)
                _addRecordEvent.postValue(Pair(eventData, null))
            } catch (e: Exception) {
                _addRecordEvent.postValue(Pair(null, e))
            }
        }
    }
}
