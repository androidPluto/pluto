package com.pluto.plugins.logger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.logger.internal.LogTimeStamp
import com.pluto.plugins.logger.internal.LogType
import com.pluto.plugins.logger.internal.Session
import com.pluto.utilities.selector.SelectorOption

internal class FilterViewModel(application: Application) : AndroidViewModel(application) {
    val selectedFiltersList: LiveData<List<LogType>>
        get() = _selectedFiltersList
    private val _selectedFiltersList = MutableLiveData<List<LogType>>()

    val selectedTimeStamp: LiveData<LogTimeStamp>
        get() = _selectedTimeStamp
    private val _selectedTimeStamp = MutableLiveData<LogTimeStamp>()

    val searchTextLogger: LiveData<String>
        get() = _searchTextLogger
    private val _searchTextLogger = MutableLiveData<String>()

    private val preferences = Preferences(application)
    private val logTypes = listOf(
        LogType("debug"),
        LogType("verbose"),
        LogType("error"),
        LogType("info"),
        LogType("event")
    )

    private val timeStamps = listOf(
        LogTimeStamp(1),
        LogTimeStamp(5),
        LogTimeStamp(10),
        LogTimeStamp(Integer.MIN_VALUE, true)
    )

    val isTriggerSearch: LiveData<Boolean>
        get() = _isTriggerSearch
    private val _isTriggerSearch = MediatorLiveData<Boolean>()
    val isFilterApplied: LiveData<Boolean>
        get() = _isFilterApplied
    private val _isFilterApplied = MediatorLiveData<Boolean>()

    val isFilterVisible: LiveData<Boolean>
        get() = _isFilterVisible
    private val _isFilterVisible = MediatorLiveData<Boolean>()

    init {

        _isTriggerSearch.addSource(_selectedFiltersList) { _isTriggerSearch.postValue(true) }
        _isTriggerSearch.addSource(_searchTextLogger) { _isTriggerSearch.postValue(true) }
        _isTriggerSearch.addSource(_selectedTimeStamp) { _isTriggerSearch.postValue(true) }
        _searchTextLogger.postValue(Session.loggerSearchText)
        _selectedFiltersList.postValue(preferences.selectedFilterLogType)
        _selectedTimeStamp.postValue(preferences.selectedFilterTime)
        _isFilterApplied.addSource(_selectedFiltersList) {
            if (it.isNotEmpty() || getSelectedTimeStamp().timeStamp != 0) {
                _isFilterApplied.postValue(true)
            } else {
                _isFilterApplied.postValue(false)
            }
        }
        _isFilterApplied.addSource(_selectedTimeStamp) {
            if (it.timeStamp != 0 || getSelectedFilters().isNotEmpty()) {
                _isFilterApplied.postValue(true)
            } else {
                _isFilterApplied.postValue(false)
            }
        }
        _isFilterVisible.postValue(false)
    }

    fun getLogTypes(): List<SelectorOption> {
        return logTypes
    }

    fun getTimeStamps(): List<SelectorOption> {
        return timeStamps
    }

    fun getSelectedFilters(): List<LogType> {
        return selectedFiltersList.value ?: emptyList()
    }

    fun getSelectedTimeStamp(): LogTimeStamp {
        return selectedTimeStamp.value ?: LogTimeStamp(0, false)
    }

    fun updateSearchText(searchText: String) {
        _searchTextLogger.postValue(searchText)
        Session.loggerSearchText = searchText
    }

    fun setSelectedFiltersLogType(logTypeList: ArrayList<LogType>) {
        _selectedFiltersList.postValue(logTypeList)
        preferences.selectedFilterLogType = logTypeList
    }
    fun setSelectedFilterTimeStamp(logTimeStamp: LogTimeStamp) {
        _selectedTimeStamp.postValue(logTimeStamp)
        preferences.selectedFilterTime = logTimeStamp
    }
    fun getSearchText(): String {
        return searchTextLogger.value ?: ""
    }

    fun toggleFilterViewVisibility() {
        _isFilterVisible.postValue(_isFilterVisible.value?.not())
    }
    fun clearFilters() {
        preferences.clearFilters()
        _selectedFiltersList.postValue(emptyList())
        _selectedTimeStamp.postValue(LogTimeStamp(0, false))
    }
}
