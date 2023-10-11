package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.HorizontalScrollView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.RoomDatabase
import com.pluto.plugins.rooms.db.PlutoRoomsDBWatcher.LOG_TAG
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDbDetailsBinding
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_ADD_UPDATE_REQUEST
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_FETCH_CONTENT
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_FETCH_TABLES
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.ProcessedTableContents
import com.pluto.plugins.rooms.db.internal.RowAction
import com.pluto.plugins.rooms.db.internal.RowDetailsData
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.plugins.rooms.db.internal.UIViewModel
import com.pluto.plugins.rooms.db.internal.core.query.Query
import com.pluto.plugins.rooms.db.internal.ui.ColumnDetailsFragment.Companion.ATTR_COLUMN
import com.pluto.plugins.rooms.db.internal.ui.ColumnDetailsFragment.Companion.ATTR_TABLE
import com.pluto.utilities.DebugLog
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.delayedLaunchWhenResumed
import com.pluto.utilities.extensions.forEachIndexed
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.showMoreOptions
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.share.ContentShareViewModel
import com.pluto.utilities.share.ShareAction
import com.pluto.utilities.share.Shareable
import com.pluto.utilities.share.csv.CSVFormatter
import com.pluto.utilities.share.lazyContentSharer
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding
import java.lang.Exception
import java.lang.StringBuilder

internal class DetailsFragment : Fragment(R.layout.pluto_rooms___fragment_db_details) {

    private val binding by viewBinding(PlutoRoomsFragmentDbDetailsBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by viewModels()
    private val sharer: ContentShareViewModel by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        convertArguments(arguments)?.let { dbConfig ->
            onBackPressed { findNavController().navigateUp() }
            viewModel.initDBSession(requireContext(), dbConfig.name, dbConfig.dbClass)
            binding.dbName.setSpan {
                append(getString(R.string.pluto_rooms___db_title))
                append(bold(" ${dbConfig.name}".uppercase()))
            }

            binding.table.setOnDebounceClickListener { openTableSelector() }
            binding.alert.setOnDebounceClickListener {
                context?.toast(getString(R.string.pluto_rooms___system_table_error))
            }
            binding.close.setOnDebounceClickListener { requireActivity().onBackPressed() }
            binding.options.setOnDebounceClickListener {
                viewModel.currentTable.value?.let { table ->
                    context?.showMoreOptions(it, R.menu.pluto_rooms___menu_table_options) { item ->
                        when (item.itemId) {
                            R.id.add -> openDetailsView(table.name, -1, null, true)
                            R.id.export -> shareTableContent(table.name)
                            R.id.refresh -> viewModel.currentTable.value?.let { viewModel.selectTable(it) }
                            R.id.clear -> viewModel.clearTable(table.name)
                            R.id.schema -> openTableSchema()
                        }
                    }
                } ?: toast(getString(R.string.pluto_rooms___select_table_options))
            }
            binding.query.setOnDebounceClickListener {
                viewModel.currentTable.value?.let {
                    sharer.share(
                        Shareable(
                            title = "Share SQL Query", content = Query.Tables.values(it.name, viewModel.filters, viewModel.sortBy)
                        )
                    )
                }
            }
            binding.pullToRefresh.setOnRefreshListener {
                viewModel.currentTable.value?.let { viewModel.selectTable(it) }
                binding.pullToRefresh.isRefreshing = false
            }

            binding.applyFilter.setOnDebounceClickListener(haptic = true) {
                findNavController().navigate(R.id.openFilterView)
            }

            viewModel.currentTable.removeObserver(currentTableObserver)
            viewModel.currentTable.observe(viewLifecycleOwner, currentTableObserver)

            viewModel.rowActionEvent.removeObserver(rowClickEventObserver)
            viewModel.rowActionEvent.observe(viewLifecycleOwner, rowClickEventObserver)

            viewModel.processedTableContent.removeObserver(tableContentObserver)
            viewModel.processedTableContent.observe(viewLifecycleOwner, tableContentObserver)

            viewModel.queryError.removeObserver(errorObserver)
            viewModel.queryError.observe(viewLifecycleOwner, errorObserver)

            viewModel.rowCounts.removeObserver(rowCountObserver)
            viewModel.rowCounts.observe(viewLifecycleOwner, rowCountObserver)

            uiViewModel.tableGridView.removeObserver(tableUIObserver)
            uiViewModel.tableGridView.observe(viewLifecycleOwner, tableUIObserver)
        } ?: requireActivity().onBackPressed()
    }

    private fun shareTableContent(table: String) {
        viewModel.processedTableContent.value?.let { content ->
            sharer.performAction(
                ShareAction.ShareAsFile(
                    Shareable(
                        title = "Export $table Table", content = content.serialize(), fileName = "Export $table table"
                    ),
                    "text/csv"
                )
            )
        } ?: run {
            toast("no content found to share")
        }
    }

    private fun openTableSelector() {
        findNavController().navigate(R.id.openTableSelector)
    }

    private fun openTableSchema() {
        findNavController().navigate(R.id.openTableSchemaView)
    }

    private val rowClickEventObserver = Observer<Pair<RowAction, RowDetailsData>> {
        when (it.first) {
            is RowAction.Click -> {
                val bundle = bundleOf("data" to it.second, "isInsert" to (it.first as RowAction.Click).isInsert)
                findNavController().navigate(R.id.openDataEditor, bundle)
            }

            is RowAction.LongClick -> {
                val bundle = bundleOf("data" to it.second)
                findNavController().navigate(R.id.openActionsView, bundle)
            }

            RowAction.Duplicate -> viewLifecycleOwner.lifecycleScope.delayedLaunchWhenResumed(100L) {
                val bundle = bundleOf("data" to it.second, "isInsert" to true)
                findNavController().navigate(R.id.openDataEditor, bundle)
            }

            RowAction.Delete -> it.second.values?.let { values ->
                val values1 = arrayListOf<Pair<ColumnModel, String?>>().apply {
                    Pair(it.second.columns, values).forEachIndexed { _, column, row ->
                        add(Pair(column, row))
                    }
                }
                viewModel.deleteRow(it.second.table, values1)
            }
        }
    }

    private val errorObserver = Observer<Pair<String, Exception>> {
        handleError(it.first, it.second)
    }

    private val rowCountObserver = Observer<Pair<Int, Int?>> {
        binding.count.setSpan {
            append(bold(" ${it.first}"))
            it.second?.let {
                append("/$it")
            }
            append(" rows")
        }
        setupFilterUi()
    }

    private fun setupFilterUi() {
        if (viewModel.filters.isEmpty()) {
            binding.applyFilter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_rooms___ic_no_filter, 0, 0, 0)
            binding.applyFilter.setSpan {
                append(fontColor(getString(R.string.pluto_rooms___no_data_filter_applied), context.color(R.color.pluto___text_dark_40)))
                append(" ${bold(getString(R.string.pluto_rooms___apply_filter))}")
            }
        } else {
            binding.applyFilter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_rooms___ic_filter, 0, 0, 0)
            binding.applyFilter.setSpan {
                append(
                    fontColor(
                        String.format(
                            resources.getQuantityString(
                                R.plurals.pluto_rooms___applied_filters, viewModel.filters.size, viewModel.filters.size
                            )
                        ),
                        context.color(R.color.pluto___blue)
                    )
                )
            }
        }
    }

    private fun handleError(error: String, exception: Exception) {
        when (error) {
            ERROR_FETCH_TABLES, ERROR_FETCH_CONTENT, ERROR_ADD_UPDATE_REQUEST -> {
                findNavController().navigate(
                    R.id.openQueryErrorDialog, bundleOf(QueryErrorFragment.ERROR_MESSAGE to exception.message)
                )
                com.pluto.utilities.DebugLog.e(LOG_TAG, "error while fetching from table", exception)
            }
        }
        setupFilterUi()
    }

    private val currentTableObserver = Observer<TableModel?> { table ->
        table?.let {
            binding.alert.visibility = if (it.isSystemTable) VISIBLE else GONE
            binding.table.text = it.name
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                binding.loader.visibility = VISIBLE
                resetTableGrid()
                viewModel.fetchData(it.name)
            }
        } ?: openTableSelector()
    }

    private val tableContentObserver = Observer<ProcessedTableContents> {
        uiViewModel.generateTableGridView(
            requireContext(), it.first, it.second, viewModel.sortBy,
            onRowClick = { index, value ->
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.currentTable.value?.let { table ->
                        openDetailsView(table.name, index, value, false)
                    }
                }
            },
            onRowLongClick = { index, value ->
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.currentTable.value?.let { table ->
                        openActionsView(table.name, index, value)
                    }
                }
            },
            onColumnClick = { column ->
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.currentTable.value?.let { table ->
                        val bundle = bundleOf(ATTR_COLUMN to column, ATTR_TABLE to table)
                        findNavController().navigate(R.id.openColumnDetailsView, bundle)
                    }
                }
            },
            onColumnLongClick = { column ->
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.currentTable.value?.let { table ->
                        val bundle = bundleOf(ATTR_COLUMN to column, ATTR_TABLE to table)
                        findNavController().navigate(R.id.openColumnDetailsView, bundle)
                    }
                }
            }
        )
    }

    private val tableUIObserver = Observer<HorizontalScrollView> {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.loader.visibility = GONE
            binding.count.visibility = VISIBLE
            binding.nsv.removeAllViews()
            binding.nsv.addView(it)
        }
    }

    private fun resetTableGrid() {
        binding.nsv.scrollTo(0, 0)
        binding.nsv.removeAllViews()
        binding.count.visibility = GONE
    }

    private fun convertArguments(arguments: Bundle?): DatabaseModel? {
        return arguments?.let {
            val dbClass = it.get(DB_CLASS)
            val dbName = it.getString(DB_NAME)
            if (dbClass != null && dbName != null) {
                return DatabaseModel(dbName, dbClass as Class<out RoomDatabase>)
            }
            return null
        }
    }

    private fun openDetailsView(table: String, index: Int, list: List<String>? = null, isInsertEvent: Boolean) {
        viewModel.triggerAddRecordEvent(table, index, list, isInsertEvent)
    }

    private fun openActionsView(table: String, index: Int, list: List<String>? = null) {
        viewModel.triggerActionsOpenEvent(table, index, list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroyDBSession()
    }

    companion object {
        const val DB_CLASS = "dbClass"
        const val DB_NAME = "dbName"
    }
}

private fun ProcessedTableContents.serialize(): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append(CSVFormatter.write(first.map { it.name }.toTypedArray()))
    second.forEach {
        stringBuilder.append(CSVFormatter.write(it.toTypedArray()))
    }
    return stringBuilder.toString()
}
