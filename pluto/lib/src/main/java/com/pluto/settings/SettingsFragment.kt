package com.pluto.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.BuildConfig
import com.pluto.R
import com.pluto.databinding.PlutoFragmentSettingsBinding
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.settings.holders.SettingsGridSizeHolder.Companion.DEC_SIZE
import com.pluto.settings.holders.SettingsGridSizeHolder.Companion.INC_SIZE
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.openOverlaySettings
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.viewBinding

/**
 * Fragment that displays the settings UI as a bottom sheet dialog.
 *
 * This fragment presents various Pluto settings options to the user, including:
 * - Easy access overlay permission
 * - Easy access popup appearance
 * - Theme selection (light/dark)
 * - Grid size configuration
 * - Reset all settings option
 */
internal class SettingsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentSettingsBinding::bind)
    private val settingsAdapter: BaseAdapter by autoClearInitializer { SettingsAdapter(onActionListener) }
    private val viewModel: SettingsViewModel by activityViewModels()

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_settings, container, false)

    /**
     * Returns the theme to be used for this fragment.
     */
    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored.
     * This is where most initialization should go.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = settingsAdapter
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }
        viewModel.list.removeObserver(settingsObserver)
        viewModel.list.observe(viewLifecycleOwner, settingsObserver)
    }

    /**
     * Observer that updates the settings adapter when the list of settings items changes.
     */
    private val settingsObserver = Observer<List<ListItem>> {
        settingsAdapter.list = it
    }

    /**
     * Listener that handles actions performed on settings items.
     * This includes toggling settings, adjusting values, and triggering the reset operation.
     */
    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            when (data) {
                is SettingsEasyAccessEntity -> {
                    context?.openOverlaySettings()
                    requireActivity().finish()
                }

                is SettingsEasyAccessPopupAppearanceEntity -> {
                    when (data.type) {
                        "handed" -> {
                            val current = SettingsPreferences.isRightHandedAccessPopup
                            SettingsPreferences.isRightHandedAccessPopup = !current
                            context?.toast(context!!.getString(R.string.pluto___notch_settings_updated))
                        }

                        else -> check(!BuildConfig.DEBUG) {
                            "unsupported appearance type"
                        }
                    }
                    settingsAdapter.notifyItemChanged(holder.layoutPosition)
                }

                is SettingsGridSizeEntity -> {
                    when (action) {
                        INC_SIZE -> SettingsPreferences.gridSize++
                        DEC_SIZE -> SettingsPreferences.gridSize--
                    }
                    settingsAdapter.notifyItemChanged(holder.layoutPosition)
                }

                is SettingsThemeEntity -> {
                    val current = SettingsPreferences.isDarkThemeEnabled
                    SettingsPreferences.isDarkThemeEnabled = !current
                    context?.toast(context!!.getString(R.string.pluto___setting_theme_updated))
                    settingsAdapter.notifyItemChanged(holder.layoutPosition)
                }

                is SettingsResetAllEntity -> {
                    viewModel.resetAll()
                    context?.toast(context!!.getString(R.string.pluto___reset_all_requested))
                }
            }
        }
    }

    private companion object {
        /**
         * Padding value for the divider between settings items in the list.
         */
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
