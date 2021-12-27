package com.mocklets.pluto.logger.internal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.logger.R
import com.mocklets.pluto.plugin.PluginOptionsViewModel
import com.mocklets.pluto.plugin.utilities.extensions.toast

internal class LogsFragment : Fragment(R.layout.pluto_logger___fragment_logs) {

    private val viewModel: PluginOptionsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentOption.removeObserver(optionObserver)
        viewModel.currentOption.observe(viewLifecycleOwner, optionObserver)
    }

    private val optionObserver = Observer<String> {
        context?.toast("$it selected")
    }
}
