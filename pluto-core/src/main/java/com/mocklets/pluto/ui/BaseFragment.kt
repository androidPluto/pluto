package com.mocklets.pluto.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoFragmentBaseBinding
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding

class BaseFragment : Fragment(R.layout.pluto___fragment_base) {

    private val binding by viewBinding(PlutoFragmentBaseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settings.setDebounceClickListener {
            findNavController().navigate(R.id.openSettings)
        }
        binding.grid.setDebounceClickListener {
            findNavController().navigate(R.id.openPluginSelector)
        }

        binding.options.setDebounceClickListener {
            findNavController().navigate(R.id.openPluginMoreOptions)
        }
    }
}
