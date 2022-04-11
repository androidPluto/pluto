package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.room.RoomDatabase
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentDbDetailsBinding
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.plugins.rooms.db.internal.RoomsDBDetailsViewModel
import com.pluto.plugins.rooms.db.internal.core.isSystemTable
import com.pluto.plugins.rooms.db.internal.core.query.QueryExecutor
import java.lang.Exception

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
            QueryExecutor.init(requireContext(), dbConfig.name, dbConfig.dbClass)
            binding.dbName.setSpan {
                append(getString(R.string.pluto_rooms___db_title))
                append(bold(" ${dbConfig.name}".uppercase()))
            }
            viewModel.fetchTables()

            binding.table.setDebounceClickListener {
                findNavController().navigate(R.id.openTableSelector)
            }

            viewModel.tables.removeObserver(tableListObserver)
            viewModel.tables.observe(viewLifecycleOwner, tableListObserver)
        } ?: requireActivity().onBackPressed()
    }

    private val tableListObserver = Observer<Pair<List<String>, Exception?>> {
        it.second?.let { } ?: processTableList(it.first)
    }

    private fun processTableList(list: List<String>) {
        list.forEach {
            DebugLog.d("prateek", "$it, ${isSystemTable(it)}")
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

    companion object {
        const val DB_CLASS = "dbClass"
        const val DB_NAME = "dbName"
    }
}
