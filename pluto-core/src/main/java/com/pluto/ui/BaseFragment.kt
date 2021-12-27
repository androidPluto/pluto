package com.pluto.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pluto.R
import com.pluto.databinding.PlutoFragmentBaseBinding
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.viewBinding

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
