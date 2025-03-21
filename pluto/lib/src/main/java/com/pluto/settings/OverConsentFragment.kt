package com.pluto.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.Pluto
import com.pluto.R
import com.pluto.databinding.PlutoFragmentOverlayConsentBinding
import com.pluto.utilities.extensions.openOverlaySettings
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

/**
 * Fragment that displays a consent dialog for enabling overlay permissions.
 *
 * This fragment is shown to the user to request permission to draw over other apps,
 * which is required for certain Pluto features like the floating debug tools.
 * It presents information about why the permission is needed and provides a button
 * to navigate to the system settings screen where the user can grant the permission.
 */
internal class OverConsentFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentOverlayConsentBinding::bind)

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_overlay_consent, container, false)

    /**
     * Returns the theme to be used for this fragment.
     */
    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored.
     * This is where most initialization should go.
     *
     * Sets up the click listener for the call-to-action button that opens the system overlay settings.
     * Also marks that the consent dialog has been shown to prevent showing it again unnecessarily.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Pluto.session.isConsentAlreadyShown = true
        binding.cta.setOnDebounceClickListener {
            context?.openOverlaySettings()
        }
    }
}
