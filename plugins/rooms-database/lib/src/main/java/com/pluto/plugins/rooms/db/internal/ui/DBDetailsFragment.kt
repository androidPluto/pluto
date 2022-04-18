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
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.EditEventData
import com.pluto.plugins.rooms.db.internal.TableContents
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

            viewModel.tableContent.removeObserver(tableContentObserver)
            viewModel.tableContent.observe(viewLifecycleOwner, tableContentObserver)

            uiViewModel.dataView.removeObserver(tableUIObserver)
            uiViewModel.dataView.observe(viewLifecycleOwner, tableUIObserver)
        } ?: requireActivity().onBackPressed()
    }

    private fun shareTableContent(table: String) {
        viewModel.tableContent.value?.first?.let { content ->
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

    private val addRecordEventObserver = Observer<Pair<EditEventData?, Exception?>> {
        it.first?.let { data ->
            val bundle = bundleOf(DATA_INDEX to data.index, DATA_COLUMNS to data.columns, DATA_VALUES to data.values)
            findNavController().navigate(R.id.openDataEditor, bundle)
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

    private val tableContentObserver = Observer<Pair<TableContents?, Exception?>> {
        it.first?.let { data ->
            uiViewModel.generateView(requireContext(), data.first, data.second) { index, value ->
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.currentTable.value?.let { table ->
                        openDetailsView(table.name, index, value)
                    }
                }
            }
        }
        it.second?.let { ex ->
            requireContext().toast("Exception occurred : $ex")
            ex.printStackTrace()
        }
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

private fun TableContents.serialize(): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append(CSVFormatter.write(first.toTypedArray()))
    second.forEach {
        stringBuilder.append(CSVFormatter.write(it.toTypedArray()))
    }
    return stringBuilder.toString()
}
