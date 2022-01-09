package com.pluto.plugin

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pluto.Pluto
import com.pluto.R
import com.pluto.applifecycle.AppState
import com.pluto.databinding.PlutoLayoutPluginSelectorBinding
import com.pluto.notch.Notch
import com.pluto.plugin.list.PluginAdapter
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.settings.SettingsFragment
import com.pluto.ui.PlutoActivity

internal class PluginSelector : DialogFragment() {

    private var notch: Notch? = null
    private val binding by viewBinding(PlutoLayoutPluginSelectorBinding::bind)
    private val pluginsViewModel by activityViewModels<PluginsViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginAdapter(onActionListener) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___layout_plugin_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            adapter = pluginAdapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        }

        binding.version.setSpan {
            append(fontColor(light("v"), context.color(R.color.pluto___white_40)))
            append(regular(com.pluto.BuildConfig.VERSION_NAME))
        }

        binding.settings.setDebounceClickListener {
            SettingsFragment().show(childFragmentManager, "settings")
        }

        Pluto.appState.removeObserver(appStateListener)
        Pluto.appState.observe(viewLifecycleOwner, appStateListener)

        pluginsViewModel.plugins.removeObserver(pluginListObserver)
        pluginsViewModel.plugins.observe(viewLifecycleOwner, pluginListObserver)
    }

    fun show(it: AppCompatActivity, notch: Notch?) {
        this.notch = notch
        show(it.supportFragmentManager, FRAGMENT_TAG)
    }

    private val pluginListObserver = Observer<List<Plugin>> {
        pluginAdapter.list = it
    }

    private val appStateListener = Observer<AppState> {
        if (it is AppState.Background) {
            dismiss()
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is Plugin) {
                Pluto.currentPlugin.postValue(data)
                val intent = Intent(context, PlutoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            notch?.remove()
        }
        dialog.window?.setWindowAnimations(R.style.PlutoPluginSelectorAnimation)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        notch?.add()
    }

    private companion object {
        const val FRAGMENT_TAG = "plugin_selector"
        const val GRID_SPAN_COUNT = 4
    }
}
