package com.mocklets.pluto.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoFragmentSettingsBinding
import com.mocklets.pluto.plugin.utilities.setDebounceClickListener
import com.mocklets.pluto.plugin.utilities.viewBinding

class SettingsFragment : Fragment(R.layout.pluto___fragment_settings) {

    private val binding by viewBinding(PlutoFragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setDebounceClickListener {
            findNavController().popBackStack()
        }
    }
}
