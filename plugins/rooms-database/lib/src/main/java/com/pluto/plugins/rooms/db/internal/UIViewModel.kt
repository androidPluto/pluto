package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import android.widget.HorizontalScrollView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugin.utilities.SingleLiveEvent
import com.pluto.plugins.rooms.db.internal.core.TableGridView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class UIViewModel(application: Application) : AndroidViewModel(application) {

    val dataView: LiveData<HorizontalScrollView>
        get() = _dataView
    private val _dataView = SingleLiveEvent<HorizontalScrollView>()

    fun generateView(context: Context, columns: List<String>, rows: List<List<String>>, onClick: (Int, List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val hsv = HorizontalScrollView(context)
            TableGridView(context).create(columns, rows) { index ->
                onClick.invoke(index, rows[index])
            }.also { view -> hsv.addView(view) }
            _dataView.postValue(hsv)
        }
    }
}
