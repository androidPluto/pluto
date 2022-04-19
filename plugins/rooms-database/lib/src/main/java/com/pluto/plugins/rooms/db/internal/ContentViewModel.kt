package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugin.utilities.SingleLiveEvent
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.Executor
import com.pluto.plugins.rooms.db.internal.core.query.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class ContentViewModel(application: Application) : AndroidViewModel(application) {

    val tables: LiveData<List<TableModel>>
        get() = _tables
    private val _tables = MutableLiveData<List<TableModel>>()

    val currentTable: LiveData<TableModel>
        get() = _currentTable
    private val _currentTable = MutableLiveData<TableModel>()

    val processedTableContent: LiveData<ProcessedTableContents>
        get() = _tableContent
    private val _tableContent = SingleLiveEvent<ProcessedTableContents>()

    val addRecordEvent: LiveData<EditEventData>
        get() = _addRecordEvent
    private val _addRecordEvent = SingleLiveEvent<EditEventData>()

    val editEventState: LiveData<Boolean>
        get() = _editEventState
    private val _editEventState = SingleLiveEvent<Boolean>()

    val error: LiveData<Pair<String, Exception>>
        get() = _error
    private val _error = SingleLiveEvent<Pair<String, Exception>>()

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
                _tables.postValue(processedTableList.plus(processedSystemTableList))
            } catch (e: Exception) {
                _error.postValue(Pair(ERROR_FETCH_TABLES, e))
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
                val valueResult = Executor.instance.query(Query.Tables.values(table))
                val columnResult = Executor.instance.query(Query.Tables.columns(table))

//                val columns = columnResult.second.map { it[1] }
                val columns = columnResult.second.map {
                    ColumnModel(
                        columnId = it[COLUMN_CID_INDEX].toInt(),
                        name = it[COLUMN_NAME_INDEX],
                        type = it[COLUMN_TYPE_INDEX],
                        isNotNull = it[COLUMN_NOTNULL_INDEX].toInt() > 0,
                        defaultValue = it[COLUMN_DFLT_VALUE_INDEX],
                        isPrimaryKey = it[COLUMN_PRIMARY_KEY_INDEX].toInt() > 0
                    )
                }
                _tableContent.postValue(ProcessedTableContents(columns, valueResult.second))
            } catch (e: Exception) {
                _error.postValue(Pair(ERROR_FETCH_CONTENT, e))
            }
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun triggerAddRecordEvent(table: String, index: Int, list: List<String>?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val queryResult = Executor.instance.query(Query.Tables.columns(table))
                val eventData = EditEventData(
                    index = index,
                    table = table,
                    columns = queryResult.second.map {
                        ColumnModel(
                            columnId = it[COLUMN_CID_INDEX].toInt(),
                            name = it[COLUMN_NAME_INDEX],
                            type = it[COLUMN_TYPE_INDEX],
                            isNotNull = it[COLUMN_NOTNULL_INDEX].toInt() > 0,
                            defaultValue = it[COLUMN_DFLT_VALUE_INDEX],
                            isPrimaryKey = it[COLUMN_PRIMARY_KEY_INDEX].toInt() > 0
                        )
                    },
                    values = list
                )
                _addRecordEvent.postValue(eventData)
            } catch (e: Exception) {
                _error.postValue(Pair(ERROR_ADD_UPDATE_REQUEST, e))
            }
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun addNewRow(table: String, values: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val queryResult = Executor.instance.query(Query.Rows.insert(table, values))
                DebugLog.d("rooms-db", queryResult.toString())
                fetchData(table)
                _editEventState.postValue(true)
            } catch (e: Exception) {
                _error.postValue(Pair(ERROR_ADD_UPDATE, e))
            }
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun updateRow(table: String, columns: List<String>, prevValues: List<String?>, newValues: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val queryResult = Executor.instance.query(Query.Rows.update(table, columns, prevValues, newValues))
                DebugLog.d("rooms-db", queryResult.toString())
                fetchData(table)
                _editEventState.postValue(true)
            } catch (e: Exception) {
                _error.postValue(Pair(ERROR_ADD_UPDATE, e))
            }
        }
    }

    companion object {
        const val ERROR_FETCH_TABLES = "error_fetch_tables"
        const val ERROR_FETCH_CONTENT = "error_fetch_content"
        const val ERROR_ADD_UPDATE_REQUEST = "error_add_update_request"
        const val ERROR_ADD_UPDATE = "error_add_update"

        private const val COLUMN_CID_INDEX = 0
        private const val COLUMN_NAME_INDEX = 1
        private const val COLUMN_TYPE_INDEX = 2
        private const val COLUMN_NOTNULL_INDEX = 3
        private const val COLUMN_DFLT_VALUE_INDEX = 4
        private const val COLUMN_PRIMARY_KEY_INDEX = 5
    }
}
