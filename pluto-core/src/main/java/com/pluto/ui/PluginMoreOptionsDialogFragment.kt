package com.pluto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.R

class PluginMoreOptionsDialogFragment : BottomSheetDialogFragment() {

//    private val binding by viewBinding(PlutoFragmentPluginMoreOptionsBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_plugin_more_options, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme
}
