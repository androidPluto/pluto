package com.pluto.plugins.rooms.db.internal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.rooms.db.internal.core.query.QueryBuilder
import com.pluto.plugins.rooms.db.internal.core.query.QueryExecutor
import java.lang.Exception

internal class RoomsDBDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val tables: LiveData<Pair<List<String>, Exception?>>
        get() = _tables
    private val _tables = MutableLiveData<Pair<List<String>, Exception?>>()

    val currentTable: LiveData<String>
        get() = _currentTable
    private val _currentTable = MutableLiveData<String>()

    fun fetchTables() {
        val tables = arrayListOf<String>()
        QueryExecutor.query(
            QueryBuilder.GET_TABLE_NAMES,
            {
                it.second.forEach { list ->
                    tables.addAll(list)
                }
                _tables.postValue(Pair(tables, null))
            },
            {
                _tables.postValue(Pair(emptyList(), it))
            }
        )
    }

    fun selectTable(table: String) {
        _currentTable.postValue(table)
    }
}
