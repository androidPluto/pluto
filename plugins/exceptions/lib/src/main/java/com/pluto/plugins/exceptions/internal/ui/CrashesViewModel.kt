package com.pluto.plugins.exceptions.internal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.exceptions.internal.persistence.ExceptionDao
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity
import com.pluto.plugins.exceptions.internal.persistence.database.DatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class CrashesViewModel(application: Application) : AndroidViewModel(application) {

    private val exceptionDao: ExceptionDao by lazy { DatabaseManager(application.applicationContext).db.exceptionDao() }

    val exceptions: LiveData<List<ExceptionEntity>>
        get() = _exceptions
    private val _exceptions = MutableLiveData<List<ExceptionEntity>>()

    val currentException: LiveData<ExceptionEntity>
        get() = _currentException
    private val _currentException = MutableLiveData<ExceptionEntity>()

    fun fetchAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = exceptionDao.fetchAll() ?: arrayListOf()
            _exceptions.postValue(list)
        }
    }

    fun fetch(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionDao.fetch(id)?.let {
                _currentException.postValue(it)
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionDao.delete(id)
            _exceptions.postValue(exceptionDao.fetchAll() ?: arrayListOf())
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionDao.deleteAll()
            _exceptions.postValue(arrayListOf())
        }
    }
}
