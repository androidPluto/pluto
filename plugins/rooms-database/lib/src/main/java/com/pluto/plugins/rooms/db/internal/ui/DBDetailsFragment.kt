package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.internal.DatabaseModel

class DBDetailsFragment : Fragment(R.layout.pluto_rooms___fragment_db_details) {

    private var requestConfig: DatabaseModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestConfig = convertArguments(arguments)
    }

    private fun convertArguments(arguments: Bundle?): DatabaseModel? {
        return arguments?.let {
            val dbClass = it.get(DB_CLASS)
            val dbName = it.getString(DB_NAME)
            if (dbClass != null && dbName != null) {
                return DatabaseModel(dbName, dbClass as Class<*>)
            }
            return null
        }
    }

    companion object {
        const val DB_CLASS = "dbClass"
        const val DB_NAME = "dbName"
    }
}
