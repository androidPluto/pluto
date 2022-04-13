package com.pluto.plugins.rooms.db.internal.core

import android.content.Context
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugins.rooms.db.R

/**
 * A custom [TableLayout] class having functionality for creating table by using given rows and columns.
 *
 */
class DBRowView(context: Context) : TableLayout(context) {

    private val tableRowMinHeight by lazy {
        context.resources.getDimension(R.dimen.pluto___margin_vxlarge).toInt()
    }

    private val tableHeaderBackground by lazy {
        ContextCompat.getColor(
            context,
            R.color.pluto___dark_80
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
     * @param text column name
     * @return RowHeader
     */
    private fun rowHeader(text: String) = TextView(context)
        .apply {
            minHeight = tableRowMinHeight
            minWidth = 20f.dp.toInt()
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(tableHeaderBackground)
            setPadding(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL)
            this.text = text
            textSize = TEXT_SIZE_RECORD
            setTextColor(ContextCompat.getColor(context, R.color.pluto___app_bg))
            typeface = ResourcesCompat.getFont(context, R.font.muli_bold)
        }

    /**
     * Creates a [TextView] to display data in the table.
     *
     * @param text field value
     * @return RowData
     */
    private fun rowData(text: String) = TextView(context)
        .apply {
            minHeight = tableRowMinHeight
            minWidth = 20f.dp.toInt()
            gravity = Gravity.CENTER_VERTICAL
            setPadding(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL)
            this.text = text
            textSize = TEXT_SIZE_RECORD
            setTextColor(ContextCompat.getColor(context, R.color.pluto___text_dark))
            typeface = ResourcesCompat.getFont(context, R.font.muli)
        }

    private fun rowTableEnd() = TextView(context)
        .apply {
            minHeight = tableRowMinHeight
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
     * creates a row of tuples by using the provided values.
     *
     * @param values list of values to display
     * @param isHeader if true, creates header row, else normal row
     * @return TableRow
     */
    private fun tableRow(values: List<String>, isHeader: Boolean = false) =
        TableRow(context).apply {
            setPadding(0, 0, 0, 0)
            values.forEach {
                if (isHeader) {
                    addView(rowHeader(it))
                } else {
                    addView(rowData(it))
                }
            }
        }

    /**
     * Creates a table with provided values.
     *
     * @param column list of column names
     * @param rows list of rows, each row contains list of fields
     * @param onClick function to get called on clicking the row
     * @return [TableLayout] containing rows and columns filled with the provided values
     */
    fun create(column: List<String>, rows: List<List<String>>, onClick: (Int) -> Unit): DBRowView {
        addView(tableRow(column, true))
        rows.forEachIndexed { index, list ->
            val tableRow = tableRow(list, false).apply {
                setOnClickListener { onClick(index) }
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
    }
}
