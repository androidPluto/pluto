package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugins.network.R

class ContentFormatterFragment : Fragment(R.layout.pluto_network___fragment_content_formatter) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
    }
}
