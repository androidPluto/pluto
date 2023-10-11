package com.pluto.plugins.rooms.db.internal

import android.app.Application
import android.content.Context
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.rooms.db.internal.core.widgets.DataEditWidget
import com.pluto.plugins.rooms.db.internal.core.widgets.TableGridView
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.extensions.forEachIndexed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class UIViewModel(application: Application) : AndroidViewModel(application) {

    val tableGridView: LiveData<HorizontalScrollView>
        get() = _tableGridView
    private val _tableGridView = com.pluto.utilities.SingleLiveEvent<HorizontalScrollView>()

    val rowEditView: LiveData<Pair<List<DataEditWidget>, LinearLayout>>
        get() = _rowEditView
    private val _rowEditView = com.pluto.utilities.SingleLiveEvent<Pair<List<DataEditWidget>, LinearLayout>>()

    @SuppressWarnings("LongParameterList")
    fun generateTableGridView(
        context: Context,
        columns: List<ColumnModel>,
        rows: List<List<String>>,
        sortBy: Pair<String, SortBy>?,
        onRowClick: (Int, List<String>) -> Unit,
        onRowLongClick: (Int, List<String>) -> Unit,
        onColumnClick: (ColumnModel) -> Unit,
        onColumnLongClick: (ColumnModel) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val hsv = HorizontalScrollView(context)
            TableGridView(context).create(
                columns, rows, sortBy,
                onClick = { index -> // row click
                    onRowClick.invoke(index, rows[index])
                },
                onLongClick = { index ->
                    onRowLongClick.invoke(index, rows[index])
                },
                onColumnClick = { column -> // column click
                    onColumnClick.invoke(column)
                },
                onColumnLongClick = { column -> // column long click
                    onColumnLongClick.invoke(column)
                }
            ).also { view -> hsv.addView(view) }
            _tableGridView.postValue(hsv)
        }
    }

    fun generateRowEditView(context: Context, dataConfig: RowDetailsData) {
        viewModelScope.launch(Dispatchers.Default) {
            val etList = mutableListOf<DataEditWidget>()
            val mainLayout = LinearLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }

            val columns = dataConfig.columns
            val rows = dataConfig.values ?: dataConfig.columns.map { col ->
                col.defaultValue?.replace("\'", "")
            }
            Pair(columns, rows).forEachIndexed { _, column, value ->
                val valueEditText = DataEditWidget(context)
                etList.add(valueEditText)
                valueEditText.create(column, value)
                val row = LinearLayout(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.VERTICAL
                    addView(valueEditText)
                }
                mainLayout.addView(row)
            }
            _rowEditView.postValue(Pair(etList, mainLayout))
        }
    }
}
