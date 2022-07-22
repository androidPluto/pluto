package com.pluto.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.Pluto
import com.pluto.R
import com.pluto.databinding.PlutoFragmentOverlayConsentBinding
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.viewBinding

internal class OverConsentFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentOverlayConsentBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_overlay_consent, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Pluto.session.isConsentAlreadyShown = true
        binding.cta.setOnDebounceClickListener {
            context?.openOverlaySettings()
        }
    }
}
