package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDbDetailsBinding
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.RoomsDBDetailsViewModel
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.plugins.rooms.db.internal.core.DBRowView
import com.pluto.plugins.rooms.db.internal.core.query.QueryBuilder
import com.pluto.plugins.rooms.db.internal.core.query.QueryExecutor

class DBDetailsFragment : Fragment(R.layout.pluto_rooms___fragment_db_details) {

    private val binding by viewBinding(PlutoRoomsFragmentDbDetailsBinding::bind)
    private val viewModel: RoomsDBDetailsViewModel by activityViewModels()

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

            viewModel.currentTable.removeObserver(currentTableObserver)
            viewModel.currentTable.observe(viewLifecycleOwner, currentTableObserver)
        } ?: requireActivity().onBackPressed()
    }

    private fun openTableSelector() {
        findNavController().navigate(R.id.openTableSelector)
    }

    private val currentTableObserver = Observer<TableModel?> { table ->
        table?.let {
            binding.alert.visibility = if (it.isSystemTable) VISIBLE else GONE
            binding.table.text = it.name
            displayData(table.name)
        } ?: openTableSelector()
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

    private fun displayData(table: String) {
        binding.hsv.scrollTo(0, 0)
        binding.nsv.scrollTo(0, 0)
        binding.hsv.removeAllViews()
        QueryExecutor.query(
            QueryBuilder.getAllValues(table),
            { result ->
                val columns = result.first
                val rows = result.second
//            tv_record_count.text = getString(R.string.ri_label_number_of_records, rows.size)

                DBRowView(requireContext()).create(columns, rows) {
                    openDetailsView(it, columns, rows[it])
                }
                    .also { binding.hsv.addView(it) }
            },
            { ex ->
//                tv_record_count.text = ""
                context?.toast(
                    ex.toString()
//                    getString(
//                        "failed",
//                        it.message
//                    )
                )
            }
        )
    }

    private fun openDetailsView(index: Int, columns: List<String>, list: List<String>) {
        context?.toast("$index $columns $list")
    }

    companion object {
        const val DB_CLASS = "dbClass"
        const val DB_NAME = "dbName"
    }
}
