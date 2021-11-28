package com.mocklets.pluto.logger.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mocklets.pluto.logger.R
import com.mocklets.pluto.logger.databinding.PlutoLoggerFragmentDetailsBinding
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding

class DetailsFragment : Fragment(R.layout.pluto_logger___fragment_details) {

    private val binding by viewBinding(PlutoLoggerFragmentDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text.setDebounceClickListener {
            findNavController().popBackStack()
        }
    }
}
