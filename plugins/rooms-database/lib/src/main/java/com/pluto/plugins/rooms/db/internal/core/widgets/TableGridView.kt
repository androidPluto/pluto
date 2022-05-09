package com.pluto.plugins.rooms.db.internal.core.widgets

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.truncateExcess
import com.pluto.plugin.utilities.hapticFeedback
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.SortBy

/**
 * A custom [TableLayout] class having functionality for creating table by using given rows and columns.
 *
 */
internal class TableGridView(context: Context) : TableLayout(context) {

    private val tableRowHeight by lazy {
        context.resources.getDimension(R.dimen.pluto_rooms___header_height).toInt()
    }

    private val tableHeaderBackground by lazy {
        ContextCompat.getColor(
            context,
            R.color.pluto___dark_80
        )
    }

    private val tableHeaderSortedBackground by lazy {
        ContextCompat.getColor(
            context,
            R.color.pluto___dark
        )
    }

    private val tableRowBackground by lazy {
        ContextCompat.getColor(
            context,
            R.color.pluto___app_bg
        )
    }

    /**
     * Creates a [TextView] to display column names in the table.
     *
     * @param column column data
     * @param sortBy sorting logic
     * @param onColumnClick function to get called on clicking a column item
     * @param onColumnLongClick function to get called on long pressing a column item
     * @return RowHeader
     */
    private fun rowHeader(column: ColumnModel, sortBy: SortBy?, onColumnClick: (ColumnModel) -> Unit, onColumnLongClick: (ColumnModel) -> Unit) =
        TextView(context).apply {
            height = tableRowHeight
            minWidth = 20f.dp.toInt()
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(tableHeaderBackground)
            setPadding(PADDING_HORIZONTAL, 0, PADDING_HORIZONTAL, 0)
            text = column.name.truncateExcess(TEXT_OFFSET)
            textSize = TEXT_SIZE_RECORD
            setTextColor(ContextCompat.getColor(context, R.color.pluto___app_bg))
            typeface = ResourcesCompat.getFont(
                context,
                if (column.isPrimaryKey) {
                    R.font.muli_bold
                } else {
                    R.font.muli_semibold
                }
            )
            if (column.isPrimaryKey) {
                paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
            sortBy?.let {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, it.indicator, 0)
                typeface = ResourcesCompat.getFont(context, R.font.muli_bold)
                setBackgroundColor(tableHeaderSortedBackground)
            }
            setDebounceClickListener(haptic = true) {
                onColumnClick.invoke(column)
            }
            setOnLongClickListener {
                onColumnLongClick.invoke(column)
                true
            }
        }

    /**
     * Creates a [TextView] to display data in the table.
     *
     * @param text field value
     * @return RowData
     */
    private fun rowData(text: String?) = TextView(context)
        .apply {
            minHeight = tableRowHeight
            minWidth = 20f.dp.toInt()
            gravity = Gravity.CENTER_VERTICAL
            setPadding(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL)
            setSpan {
                text?.let {
                    append(it.truncateExcess(TEXT_OFFSET))
                } ?: run {
                    append(light(italic(fontColor("null", context.color(R.color.pluto___text_dark_20)))))
                }
            }
            textSize = TEXT_SIZE_RECORD
            setTextColor(ContextCompat.getColor(context, R.color.pluto___text_dark))
            typeface = ResourcesCompat.getFont(context, R.font.muli)
        }

    private fun rowTableEnd() = TextView(context)
        .apply {
            minHeight = tableRowHeight
            minWidth = 20f.dp.toInt()
            gravity = Gravity.CENTER_VERTICAL
            setPadding(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL)
            this.text = context.getString(R.string.pluto_rooms___end_of_table)
            textSize = TEXT_SIZE_EOT
            setBackgroundColor(tableRowBackground)
            setTextColor(ContextCompat.getColor(context, R.color.pluto___text_dark_80))
            typeface = ResourcesCompat.getFont(context, R.font.muli_semibold)
        }

    /**
     * creates a header by using the provided values.
     *
     * @param values list of values to display
     * @param sortBy sorting logic
     * @param onColumnClick function to get called on clicking a column item
     * @param onColumnLongClick function to get called on long pressing a column item
     * @return TableRow
     */
    private fun tableHeader(
        values: List<ColumnModel>,
        sortBy: Pair<String, SortBy>?,
        onColumnClick: (ColumnModel) -> Unit,
        onColumnLongClick: (ColumnModel) -> Unit
    ) =
        TableRow(context).apply {
            setPadding(0, 0, 0, 0)
            values.forEach {
                val sort = if (it.name == sortBy?.first) sortBy.second else null
                val headerColumn = rowHeader(it, sort, onColumnClick, onColumnLongClick).apply {
                    enableRippleEffect()
                }
                addView(headerColumn)
            }
        }

    /**
     * creates a row of tuples by using the provided values.
     *
     * @param values list of values to display
     * @return TableRow
     */
    private fun tableRow(values: List<String>) =
        TableRow(context).apply {
            setPadding(0, 0, 0, 0)
            values.forEach {
                addView(rowData(it))
            }
            enableRippleEffect()
        }

    /**
     * Creates a table with provided values.
     *
     * @param column list of column names
     * @param rows list of rows, each row contains list of fields
     * @param sortBy sorting logic
     * @param onClick function to get called on clicking the row
     * @param onLongClick function to get called on long pressing the row
     * @param onColumnClick function to get called on clicking a column item
     * @param onColumnLongClick function to get called on long pressing a column item
     * @return [TableLayout] containing rows and columns filled with the provided values
     */
    @SuppressWarnings("LongParameterList")
    fun create(
        column: List<ColumnModel>,
        rows: List<List<String>>,
        sortBy: Pair<String, SortBy>?,
        onClick: (Int) -> Unit,
        onLongClick: (Int) -> Unit,
        onColumnClick: (ColumnModel) -> Unit,
        onColumnLongClick: (ColumnModel) -> Unit
    ): TableGridView {
        addView(tableHeader(column, sortBy, onColumnClick, onColumnLongClick))
        rows.forEachIndexed { index, list ->
            val tableRow = tableRow(list).apply {
                setDebounceClickListener(haptic = true) { onClick(index) }
                setOnLongClickListener {
                    hapticFeedback(true)
                    onLongClick(index)
                    true
                }
                if (index % 2 != 0) {
                    setBackgroundColor(tableRowBackground)
                }
            }
            addView(tableRow)
        }
        addView(rowTableEnd())
        return this
    }

    companion object {
        val PADDING_VERTICAL = 12f.dp.toInt()
        val PADDING_HORIZONTAL = 18f.dp.toInt()
        const val TEXT_SIZE_RECORD = 14f
        const val TEXT_SIZE_EOT = 13f
        const val TEXT_OFFSET = 20
    }
}

private fun View.enableRippleEffect() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = ContextCompat.getDrawable(context, outValue.resourceId)
    }
}
