package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentContentFormatterBinding

class ContentFormatterFragment : Fragment(R.layout.pluto_network___fragment_content_formatter) {

    private val binding by viewBinding(PlutoNetworkFragmentContentFormatterBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        binding.close.setOnDebounceClickListener { requireActivity().onBackPressed() }
    }
}
