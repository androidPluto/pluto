package com.mocklets.pluto.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoLayoutPluginOptionsDialogBinding
import com.mocklets.pluto.plugin.DeveloperDetails
import com.mocklets.pluto.plugin.PluginOption
import com.mocklets.pluto.plugin.options.PluginOptionAdapter
import com.mocklets.pluto.plugin.utilities.DeviceInfo
import com.mocklets.pluto.plugin.utilities.extensions.dp
import com.mocklets.pluto.plugin.utilities.extensions.inflate
import com.mocklets.pluto.plugin.utilities.list.BaseAdapter
import com.mocklets.pluto.plugin.utilities.list.CustomItemDecorator
import com.mocklets.pluto.plugin.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem
import com.mocklets.pluto.plugin.utilities.setDebounceClickListener
import com.mocklets.pluto.plugin.utilities.spannable.createSpan

class PluginOptionsDialog(context: Context, onOptionSelected: (String) -> Unit) : BottomSheetDialog(context, R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto___layout_plugin_options_dialog)
    private val binding = PlutoLayoutPluginOptionsDialogBinding.bind(sheetView)
    private val deviceInfo = DeviceInfo(context)
    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is PluginOption) {
                onOptionSelected.invoke(data.id)
                dismiss()
            }
        }
    }
    private val optionsAdapter: BaseAdapter by lazy { PluginOptionAdapter(onActionListener) }

    init {
        setContentView(sheetView)
        (sheetView.parent as View).background =
            ColorDrawable(ContextCompat.getColor(context, R.color.pluto___transparent))

        binding.optionsList.apply {
            adapter = optionsAdapter
            addItemDecoration(CustomItemDecorator(context, LIST_DIVIDER_OFFSET.dp.toInt()))
        }

        binding.website.hint = context.createSpan {
            append(italic(context.getString(R.string.pluto___website_url_hint)))
        }

        binding.vcsLink.hint = context.createSpan {
            append(italic(context.getString(R.string.pluto___vcs_url_hint)))
        }

        setOnShowListener { dialog ->
            if (dialog is BottomSheetDialog) {
                val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    isHideable = false
                    skipCollapsed = true
                    peekHeight = deviceInfo.height
                }
            }
        }
    }

    fun show(pluginOptions: List<PluginOption>, developerDetails: DeveloperDetails?) {
        binding.noOptions.visibility = if (pluginOptions.isEmpty()) VISIBLE else GONE
        optionsAdapter.list = pluginOptions

        developerDetails?.website?.let { url ->
            binding.website.text = url
            binding.website.setDebounceClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            }
        }
        developerDetails?.vcsLink?.let { url ->
            binding.vcsLink.text = url
            binding.vcsLink.setDebounceClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            }
        }

        show()
    }

    private companion object {
        const val LIST_DIVIDER_OFFSET = 16f
    }
}
