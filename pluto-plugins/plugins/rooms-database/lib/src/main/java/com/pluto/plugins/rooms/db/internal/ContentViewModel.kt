package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.pluto.plugins.rooms.db.PlutoRoomsDBWatcher.LOG_TAG
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.ExecuteResult
import com.pluto.plugins.rooms.db.internal.core.query.Executor
import com.pluto.plugins.rooms.db.internal.core.query.Query
import com.pluto.utilities.DebugLog
import com.pluto.utilities.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class ContentViewModel(application: Application) : AndroidViewModel(application) {

    val tables: LiveData<List<TableModel>>
        get() = _tables
    private val _tables = MutableLiveData<List<TableModel>>()

    val currentTable: LiveData<TableModel>
        get() = _currentTable
    private val _currentTable = SingleLiveEvent<TableModel>()

    val processedTableContent: LiveData<ProcessedTableContents>
        get() = _tableContent
    private val _tableContent = SingleLiveEvent<ProcessedTableContents>()

    val rowActionEvent: LiveData<Pair<RowAction, RowDetailsData>>
        get() = _rowActionEvent
    private val _rowActionEvent = SingleLiveEvent<Pair<RowAction, RowDetailsData>>()

    val editEventState: LiveData<ExecuteResult.Success>
        get() = _editEventState
    private val _editEventState = SingleLiveEvent<ExecuteResult.Success>()

    val queryError: LiveData<Pair<String, Exception>>
        get() = _queryError
    private val _queryError = SingleLiveEvent<Pair<String, Exception>>()

    val editError: LiveData<Pair<String, Exception>>
        get() = _editError
    private val _editError = SingleLiveEvent<Pair<String, Exception>>()

    val rowCounts: LiveData<Pair<Int, Int?>>
        get() = _rowCounts
    private val _rowCounts = SingleLiveEvent<Pair<Int, Int?>>()

    var filterConfig: List<FilterModel> = arrayListOf()
        private set

    var sortBy: Pair<String, SortBy>? = null
        private set

    override fun onCleared() {
        super.onCleared()
        Executor.destroySession()
        _tableContent.value = null
    }

    fun initDBSession(context: Context, name: String, dbClass: Class<out RoomDatabase>) {
        Executor.initSession(context, name, dbClass)
        fetchTables()
    }

    fun destroyDBSession() {
        Executor.destroySession()
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
        if (table.name != _currentTable.value?.name) {
            sortBy = null
            filterConfig = emptyList()
        }
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

                        Executor.instance.query(Query.Tables.values(table, filterConfig, sortBy)).collect { valueResult ->
                            when (valueResult) {
                                is ExecuteResult.Success.Query -> {
                                    if (filterConfig.isNotEmpty()) {
                                        fetchRowCount(table, valueResult.data.second.size)
                                    } else {
                                        _rowCounts.postValue(Pair(valueResult.data.second.size, valueResult.data.second.size))
                                    }
                                    _tableContent.postValue(ProcessedTableContents(columns, valueResult.data.second))
                                }
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

    private fun fetchRowCount(table: String, filteredCount: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.query(Query.Tables.count(table)).collect {
                when (it) {
                    is ExecuteResult.Success.Query -> _rowCounts.postValue(Pair(filteredCount, it.data.second[0][0].toInt()))
                    is ExecuteResult.Failure -> _rowCounts.postValue(Pair(filteredCount, null))
                    else -> DebugLog.e(LOG_TAG, "fetchRowCount: invalid result")
                }
            }
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun triggerAddRecordEvent(table: String, index: Int, list: List<String>?, isInsertEvent: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.query(Query.Tables.columns(table)).collect {
                when (it) {
                    is ExecuteResult.Success.Query -> {
                        val eventData = RowDetailsData(
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
                        )
                        performAction(RowAction.Click(isInsertEvent), eventData)
                    }
                    is ExecuteResult.Failure -> _queryError.postValue(Pair(ERROR_ADD_UPDATE_REQUEST, it.exception))
                    else -> DebugLog.e(LOG_TAG, "triggerAddRecordEvent: invalid result")
                }
            }
        }
    }

    fun triggerActionsOpenEvent(table: String, index: Int, list: List<String>?) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.query(Query.Tables.columns(table)).collect {
                when (it) {
                    is ExecuteResult.Success.Query -> {
                        val eventData = RowDetailsData(
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
                        )
                        performAction(RowAction.LongClick, eventData)
                    }
                    is ExecuteResult.Failure -> _queryError.postValue(Pair(ERROR_ADD_UPDATE_REQUEST, it.exception))
                    else -> DebugLog.e(LOG_TAG, "triggerAddRecordEvent: invalid result")
                }
            }
        }
    }

    fun performAction(action: RowAction, data: RowDetailsData) {
        _rowActionEvent.postValue(Pair(action, data))
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

    fun deleteRow(table: String, values: List<Pair<ColumnModel, String?>>) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.delete(table, values).collect {
                when (it) {
                    is ExecuteResult.Success.Delete -> {
                        fetchData(table)
                        _editEventState.postValue(it)
                    }
                    is ExecuteResult.Failure -> _editError.postValue(Pair(ERROR_ADD_UPDATE, it.exception))
                    else -> DebugLog.e(LOG_TAG, "updateRow: invalid result")
                }
            }
        }
    }

    fun clearTable(table: String) {
        viewModelScope.launch(Dispatchers.Default) {
            Executor.instance.query(Query.Tables.clear(table)).collect {
                when (it) {
                    is ExecuteResult.Success.Query -> {
                        fetchData(table)
                        _editEventState.postValue(it)
                    }
                    is ExecuteResult.Failure -> _editError.postValue(Pair(ERROR_ADD_UPDATE, it.exception))
                    else -> DebugLog.e(LOG_TAG, "clearTable: invalid result")
                }
            }
        }
    }

    fun setSortBy(column: ColumnModel, sort: SortBy) {
        sortBy = Pair(column.name, sort)
    }

    fun clearSortBy() {
        sortBy = null
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
