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
import com.pluto.plugins.rooms.db.PlutoRoomsDB.LOG_TAG
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.ExecuteResult
import com.pluto.plugins.rooms.db.internal.core.query.Executor
import com.pluto.plugins.rooms.db.internal.core.query.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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

    val editEventState: LiveData<ExecuteResult.Success>
        get() = _editEventState
    private val _editEventState = SingleLiveEvent<ExecuteResult.Success>()

    val queryError: LiveData<Pair<String, Exception>>
        get() = _queryError
    private val _queryError = SingleLiveEvent<Pair<String, Exception>>()

    val editError: LiveData<Pair<String, Exception>>
        get() = _editError
    private val _editError = SingleLiveEvent<Pair<String, Exception>>()

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
        viewModelScope.launch(Dispatchers.Default) {
            val tables = arrayListOf<String>()
            Executor.instance.query(Query.Database.GET_TABLE_NAMES).collect {
                when (it) {
                    is ExecuteResult.Success.Query -> {
                        it.data.second.forEach { list ->
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
                    }
                    is ExecuteResult.Failure -> _queryError.postValue(Pair(ERROR_FETCH_TABLES, it.exception))
                    else -> DebugLog.e(LOG_TAG, "fetchTables: invalid result")
                }
            }
        }
    }

    fun selectTable(table: TableModel) {
        _currentTable.postValue(table)
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun fetchData(table: String) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.query(Query.Tables.columns(table)).collect { columnResult ->
                when (columnResult) {
                    is ExecuteResult.Success.Query -> {
                        val columns = columnResult.data.second.map { col ->
                            ColumnModel(
                                columnId = col[COLUMN_CID_INDEX].toInt(),
                                name = col[COLUMN_NAME_INDEX],
                                type = col[COLUMN_TYPE_INDEX],
                                isNotNull = col[COLUMN_NOTNULL_INDEX].toInt() > 0,
                                defaultValue = col[COLUMN_DFLT_VALUE_INDEX],
                                isPrimaryKey = col[COLUMN_PRIMARY_KEY_INDEX].toInt() > 0
                            )
                        }

                        Executor.instance.query(Query.Tables.values(table)).collect { valueResult ->
                            when (valueResult) {
                                is ExecuteResult.Success.Query -> _tableContent.postValue(ProcessedTableContents(columns, valueResult.data.second))
                                is ExecuteResult.Failure -> _queryError.postValue(Pair(ERROR_FETCH_CONTENT, valueResult.exception))
                                else -> DebugLog.e(LOG_TAG, "fetch values: invalid result")
                            }
                        }
                    }
                    is ExecuteResult.Failure -> _queryError.postValue(Pair(ERROR_FETCH_CONTENT, columnResult.exception))
                    else -> DebugLog.e(LOG_TAG, "fetch column: invalid result")
                }
            }
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun triggerAddRecordEvent(table: String, index: Int, list: List<String>?) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.query(Query.Tables.columns(table)).collect {
                when (it) {
                    is ExecuteResult.Success.Query -> {
                        val eventData = EditEventData(
                            index = index,
                            table = table,
                            columns = it.data.second.map { col ->
                                ColumnModel(
                                    columnId = col[COLUMN_CID_INDEX].toInt(),
                                    name = col[COLUMN_NAME_INDEX],
                                    type = col[COLUMN_TYPE_INDEX],
                                    isNotNull = col[COLUMN_NOTNULL_INDEX].toInt() > 0,
                                    defaultValue = col[COLUMN_DFLT_VALUE_INDEX],
                                    isPrimaryKey = col[COLUMN_PRIMARY_KEY_INDEX].toInt() > 0
                                )
                            },
                            values = list,
                            isInsertEvent = list == null
                        )
                        _addRecordEvent.postValue(eventData)
                    }
                    is ExecuteResult.Failure -> _queryError.postValue(Pair(ERROR_ADD_UPDATE_REQUEST, it.exception))
                    else -> DebugLog.e(LOG_TAG, "triggerAddRecordEvent: invalid result")
                }
            }
        }
    }

    fun addNewRow(table: String, values: List<Pair<ColumnModel, String?>>) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.insert(table, values).collect {
                when (it) {
                    is ExecuteResult.Success.Insert -> {
                        fetchData(table)
                        _editEventState.postValue(it)
                    }
                    is ExecuteResult.Failure -> _editError.postValue(Pair(ERROR_ADD_UPDATE, it.exception))
                    else -> DebugLog.e(LOG_TAG, "addNewRow: invalid result")
                }
            }
        }
    }

    @SuppressWarnings("UnusedPrivateMember")
    fun updateRow(table: String, newValues: List<Pair<ColumnModel, String?>>, prevValues: ArrayList<Pair<ColumnModel, String?>>) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.update(table, newValues, prevValues).collect {
                when (it) {
                    is ExecuteResult.Success.Update -> {
                        fetchData(table)
                        _editEventState.postValue(it)
                    }
                    is ExecuteResult.Failure -> _editError.postValue(Pair(ERROR_ADD_UPDATE, it.exception))
                    else -> DebugLog.e(LOG_TAG, "updateRow: invalid result")
                }
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
