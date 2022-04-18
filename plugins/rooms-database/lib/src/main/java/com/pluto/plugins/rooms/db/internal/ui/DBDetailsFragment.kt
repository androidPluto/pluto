package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.HorizontalScrollView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.extensions.showMoreOptions
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.sharing.ContentShareViewModel
import com.pluto.plugin.utilities.sharing.ShareAction
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.csv.CSVFormatter
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDbDetailsBinding
import com.pluto.plugins.rooms.db.internal.ContentViewModel
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_ADD_UPDATE_EVENT
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_FETCH_CONTENT
import com.pluto.plugins.rooms.db.internal.ContentViewModel.Companion.ERROR_FETCH_TABLES
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.EditEventData
import com.pluto.plugins.rooms.db.internal.ProcessedTableContents
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.plugins.rooms.db.internal.UIViewModel
import com.pluto.plugins.rooms.db.internal.core.query.Executor
import com.pluto.plugins.rooms.db.internal.ui.DataEditFragment.Companion.DATA_COLUMNS
import com.pluto.plugins.rooms.db.internal.ui.DataEditFragment.Companion.DATA_INDEX
import com.pluto.plugins.rooms.db.internal.ui.DataEditFragment.Companion.DATA_VALUES
import java.lang.Exception
import java.lang.StringBuilder

class DBDetailsFragment : Fragment(R.layout.pluto_rooms___fragment_db_details) {

    private val binding by viewBinding(PlutoRoomsFragmentDbDetailsBinding::bind)
    private val viewModel: ContentViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by viewModels()
    private val sharer: ContentShareViewModel by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        convertArguments(arguments)?.let { dbConfig ->
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        findNavController().navigateUp()
                    }
                }
            )
            viewModel.init(requireContext(), dbConfig.name, dbConfig.dbClass)
            binding.dbName.setSpan {
                append(getString(R.string.pluto_rooms___db_title))
                append(bold(" ${dbConfig.name}".uppercase()))
            }

            binding.table.setDebounceClickListener {
                openTableSelector()
            }
            binding.alert.setDebounceClickListener {
                context?.toast(getString(R.string.pluto_rooms___system_table_error))
            }
            binding.close.setDebounceClickListener {
                requireActivity().onBackPressed()
            }
            binding.options.setDebounceClickListener {
                viewModel.currentTable.value?.let { table ->
                    context?.showMoreOptions(it, R.menu.pluto_rooms___menu_table_options) { item ->
                        when (item.itemId) {
                            R.id.add -> openDetailsView(table.name, -1) // viewModel.triggerAddRecordEvent(table.name)
                            R.id.export -> shareTableContent(table.name)
                            R.id.refresh -> viewModel.currentTable.value?.let { viewModel.selectTable(it) }
                        }
                    }
                } ?: toast(getString(R.string.pluto_rooms___select_table_options))
            }

            viewModel.currentTable.removeObserver(currentTableObserver)
            viewModel.currentTable.observe(viewLifecycleOwner, currentTableObserver)

            viewModel.addRecordEvent.removeObserver(addRecordEventObserver)
            viewModel.addRecordEvent.observe(viewLifecycleOwner, addRecordEventObserver)

            viewModel.processedTableContent.removeObserver(tableContentObserver)
            viewModel.processedTableContent.observe(viewLifecycleOwner, tableContentObserver)

            viewModel.error.removeObserver(errorObserver)
            viewModel.error.observe(viewLifecycleOwner, errorObserver)

            uiViewModel.dataView.removeObserver(tableUIObserver)
            uiViewModel.dataView.observe(viewLifecycleOwner, tableUIObserver)
        } ?: requireActivity().onBackPressed()
    }

    private fun shareTableContent(table: String) {
        viewModel.processedTableContent.value?.let { content ->
            sharer.performAction(
                ShareAction.ShareAsFile(
                    Shareable(
                        title = "Export $table Table",
                        content = content.serialize(),
                        fileName = "Export $table table"
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

    private val addRecordEventObserver = Observer<EditEventData> {
        val bundle = bundleOf(DATA_INDEX to it.index, DATA_COLUMNS to it.columns, DATA_VALUES to it.values)
        findNavController().navigate(R.id.openDataEditor, bundle)
    }

    private val errorObserver = Observer<Pair<String, Exception>> {
        handleError(it.first, it.second)
    }

    private fun handleError(error: String, exception: Exception) {
        when (error) {
            ERROR_FETCH_TABLES, ERROR_FETCH_CONTENT, ERROR_ADD_UPDATE_EVENT -> {
                toast("Error (see logs) : ${exception.message}")
                exception.printStackTrace()
            }
        }
    }

    private val currentTableObserver = Observer<TableModel?> { table ->
        table?.let {
            binding.alert.visibility = if (it.isSystemTable) VISIBLE else GONE
            binding.table.text = it.name
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.fetchData(it.name)
            }
        } ?: openTableSelector()
    }

    private val tableContentObserver = Observer<ProcessedTableContents> {
        uiViewModel.generateView(
            requireContext(), it.first, it.second,
            { index, value -> // row click
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.currentTable.value?.let { table ->
                        openDetailsView(table.name, index, value)
                    }
                }
            },
            { column -> // column click
                toast("try sorting")
            },
            { column -> // column long click
                toast("${column.type} ${column.isPrimaryKey}")
            }
        )
    }

    private val tableUIObserver = Observer<HorizontalScrollView> {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.nsv.scrollTo(0, 0)
            binding.nsv.removeAllViews()
            binding.nsv.addView(it)
        }
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

    private fun openDetailsView(table: String, index: Int, list: List<String>? = null) {
        viewModel.triggerAddRecordEvent(table, index, list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Executor.destroySession()
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
