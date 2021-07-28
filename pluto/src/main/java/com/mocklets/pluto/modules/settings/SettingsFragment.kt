package com.mocklets.pluto.modules.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.BuildConfig
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.dp
import com.mocklets.pluto.core.extensions.openOverlaySettings
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.preferences.Preferences
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.CustomItemDecorator
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.routing.Screens
import com.mocklets.pluto.core.ui.routing.lazyRouter
import com.mocklets.pluto.core.viewBinding
import com.mocklets.pluto.databinding.PlutoFragmentSettingsBinding
import com.mocklets.pluto.modules.exceptions.ui.CrashesViewModel

internal class SettingsFragment : Fragment(R.layout.pluto___fragment_settings) {

    private val binding by viewBinding(PlutoFragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by viewModels()
    private val exceptionViewModel: CrashesViewModel by activityViewModels()
    private val router by lazyRouter()
    private val settingsAdapter: BaseAdapter by lazy { SettingsAdapter(onActionListener) }

    override fun onResume() {
        super.onResume()
        viewModel.generate(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = settingsAdapter
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }
        viewModel.list.removeObserver(settingsObserver)
        viewModel.list.observe(viewLifecycleOwner, settingsObserver)

        binding.about.setDebounceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pluto.mocklets.com"))
            startActivity(browserIntent)
        }

        binding.version.setSpan {
            append(fontColor(regular("v "), context.color(R.color.pluto___text_dark_40)))
            append(semiBold(BuildConfig.VERSION_NAME))
        }

        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }
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
                            val current = Preferences(context!!).isDarkAccessPopup
                            Preferences(context!!).isDarkAccessPopup = !current
                        }
                        "handed" -> {
                            val current = Preferences(context!!).isRightHandedAccessPopup
                            Preferences(context!!).isRightHandedAccessPopup = !current
                        }
                        else -> {
                            check(!BuildConfig.DEBUG) {
                                "unsupported appearance type"
                            }
                        }
                    }
                    settingsAdapter.notifyItemChanged(holder?.absoluteAdapterPosition ?: 0)
                }
                is SettingsSharedPrefEntity -> router.navigate(Screens.SharedPrefFilter)
                is SettingsResetAllEntity -> {
                    viewModel.resetAll()
                    exceptionViewModel.deleteAll()
                    context?.toast(context!!.getString(R.string.pluto___reset_all_success_message))
                }
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
