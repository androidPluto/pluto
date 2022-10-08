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
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.viewBinding

internal class SettingsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentSettingsBinding::bind)
    private val settingsAdapter: BaseAdapter by lazy { SettingsAdapter(onActionListener) }
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_settings, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = settingsAdapter
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }
        viewModel.list.removeObserver(settingsObserver)
        viewModel.list.observe(viewLifecycleOwner, settingsObserver)
    }

    private val settingsObserver = Observer<List<ListItem>> {
        settingsAdapter.list = it
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            when (data) {
                is SettingsEasyAccessEntity -> context?.openOverlaySettings()
                is SettingsEasyAccessPopupAppearanceEntity -> {
                    when (data.type) {
                        "mode" -> {
                            val current = SettingsPreferences.isDarkAccessPopup
                            SettingsPreferences.isDarkAccessPopup = !current
                            context?.toast(context!!.getString(R.string.pluto___notch_settings_updated))
                        }
                        "handed" -> {
                            val current = SettingsPreferences.isRightHandedAccessPopup
                            SettingsPreferences.isRightHandedAccessPopup = !current
                            context?.toast(context!!.getString(R.string.pluto___notch_settings_updated))
                        }
                        else -> check(!BuildConfig.DEBUG) {
                            "unsupported appearance type"
                        }
                    }
                    settingsAdapter.notifyItemChanged(holder?.adapterPosition ?: 0)
                }
                is SettingsResetAllEntity -> {
                    viewModel.resetAll()
                    context?.toast(context!!.getString(R.string.pluto___reset_all_requested))
                }
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
