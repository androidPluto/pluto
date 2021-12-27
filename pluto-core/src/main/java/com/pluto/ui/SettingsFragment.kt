package com.pluto.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pluto.R
import com.pluto.databinding.PlutoFragmentSettingsBinding
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.viewBinding

class SettingsFragment : Fragment(R.layout.pluto___fragment_settings) {

    private val binding by viewBinding(PlutoFragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setDebounceClickListener {
            findNavController().popBackStack()
        }
    }
}
