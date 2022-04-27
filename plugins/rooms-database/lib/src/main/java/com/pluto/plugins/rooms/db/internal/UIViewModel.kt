package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import android.widget.HorizontalScrollView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugin.utilities.SingleLiveEvent
import com.pluto.plugins.rooms.db.internal.core.widgets.TableGridView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class UIViewModel(application: Application) : AndroidViewModel(application) {

    val dataView: LiveData<HorizontalScrollView>
        get() = _dataView
    private val _dataView = SingleLiveEvent<HorizontalScrollView>()

    @SuppressWarnings("LongParameterList")
    fun generateView(
        context: Context,
        columns: List<ColumnModel>,
        rows: List<List<String>>,
        onRowClick: (Int, List<String>) -> Unit,
        onColumnClick: (ColumnModel) -> Unit,
        onColumnLongClick: (ColumnModel) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val hsv = HorizontalScrollView(context)
            TableGridView(context).create(
                columns, rows,
                { index -> // row click
                    onRowClick.invoke(index, rows[index])
                },
                { column -> // column click
                    onColumnClick.invoke(column)
                },
                { column -> // column long click
                    onColumnLongClick.invoke(column)
                }
            ).also { view -> hsv.addView(view) }
            _dataView.postValue(hsv)
        }
    }
}
