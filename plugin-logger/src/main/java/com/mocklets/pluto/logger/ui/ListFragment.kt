package com.mocklets.pluto.logger.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mocklets.pluto.logger.R
import com.mocklets.pluto.logger.databinding.PlutoLoggerFragmentListBinding
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding

class ListFragment : Fragment(R.layout.pluto_logger___fragment_list) {

    private val binding by viewBinding(PlutoLoggerFragmentListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setDebounceClickListener {
            findNavController().navigate(R.id.openDetails)
        }
    }
}
