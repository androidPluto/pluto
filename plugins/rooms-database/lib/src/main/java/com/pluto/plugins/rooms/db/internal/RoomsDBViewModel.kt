package com.pluto.plugins.rooms.db.internal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.rooms.db.PlutoRoomsDBWatcher

internal class RoomsDBViewModel(application: Application) : AndroidViewModel(application) {

    val dbs: LiveData<List<DatabaseModel>>
        get() = _dbs
    private val _dbs = MutableLiveData<List<DatabaseModel>>()

    fun fetch() {
        _dbs.postValue(PlutoRoomsDBWatcher.watchList.value.toList())
    }
}
