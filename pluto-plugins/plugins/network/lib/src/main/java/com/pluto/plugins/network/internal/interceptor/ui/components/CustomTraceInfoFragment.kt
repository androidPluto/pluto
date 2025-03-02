package com.pluto.plugins.network.internal.interceptor.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.network.R

internal class CustomTraceInfoFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_network___dialog_custom_traces_info, container, false)

    override fun getTheme(): Int = R.style.PlutoNetworkBottomSheetDialog
}
