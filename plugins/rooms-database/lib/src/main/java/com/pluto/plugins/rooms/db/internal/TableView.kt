package com.pluto.plugins.rooms.db.internal

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pluto.plugins.rooms.db.R

/**
 * A custom [TableLayout] class having functionality for creating table by using given rows and columns.
 *
 */
class TableView(context: Context) : TableLayout(context) {

    private val tableRowMinHeight by lazy {
        context.resources.getDimension(R.dimen.pluto___margin_vxlarge).toInt()
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
     * @param text column name
     * @return TableHeader
     */
    private fun tableHeader(text: String) = TextView(context)
        .apply {
            minHeight = tableRowMinHeight
            gravity = Gravity.CENTER_VERTICAL
            setPadding(PADDING, PADDING, PADDING, PADDING)
            this.text = text
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT_BOLD
        }

    /**
     * Creates a [TextView] to display values in the table.
     *
     * @param text field value
     * @return TableTuple
     */
    private fun tableTuple(text: String) = TextView(context)
        .apply {
            minHeight = tableRowMinHeight
            gravity = Gravity.CENTER_VERTICAL
            setPadding(PADDING, PADDING, PADDING, PADDING)
            this.text = text
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT
        }

    /**
     * creates a row of tuples by using the provided values.
     *
     * @param values list of values to display
     * @param isHeader if true, creates header row, else normal row
     * @return TableRow
     */
    private fun tableRow(values: List<String>, isHeader: Boolean = false) =
        TableRow(context).apply {
            setPadding(0, 2, 0, 2)
            values.forEach {
                if (isHeader) {
                    addView(tableHeader(it))
                } else {
                    addView(tableTuple(it))
                }
            }
        }

    /**
     * Creates a table with provided values.
     *
     * @param columnNames list of column names
     * @param rows list of rows, each row contains list of fields
     * @param onClickAction function to get called on clicking the row
     * @param onLongClickAction function to get called on long clicking the row
     * @return [TableLayout] containing rows and columns filled with the provided values
     */
    fun create(
        columnNames: List<String>,
        rows: List<List<String>>,
        onClickAction: (pos: Int) -> Unit,
        onLongClickAction: (pos: Int) -> Unit
    ): TableView {
        addView(tableRow(columnNames, true))
        rows.forEachIndexed { index, list ->
            val tableRow = tableRow(list, false).apply {
                setOnClickListener { onClickAction(index) }
                setOnLongClickListener {
                    onLongClickAction(index)
                    true
                }
                if (index % 2 != 0) {
                    setBackgroundColor(tableRowBackground)
                }
            }
            addView(tableRow)
        }
        return this
    }

    companion object {
        const val PADDING = 10
    }
}
