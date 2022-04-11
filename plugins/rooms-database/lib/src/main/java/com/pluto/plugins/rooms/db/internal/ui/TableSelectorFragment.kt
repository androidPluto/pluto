package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R

class TableSelectorFragment : BottomSheetDialogFragment() {

//    private val binding by viewBinding(PlutoRoomsFragmentTableSelectorBinding::bind)
//    private val viewModel: RoomsDBDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_table_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog
}
