package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.pluto.plugin.utilities.extensions.capitalizeText
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugin.utilities.views.KeyValuePairView
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsDeviceBinding
import com.pluto.plugins.exceptions.internal.DeviceInfo

internal class CrashItemDetailsDeviceHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_device), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsDeviceBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is DeviceInfo) {
            binding.appContainer.setupAppStateUI(item)
            binding.deviceContainer.setupDeviceInfoUI(item)
        }
    }

    private fun LinearLayoutCompat.setupAppStateUI(item: DeviceInfo) {
        addView(
            KeyValuePairView(context).apply {
                set(
                    context.getString(R.string.pluto_excep___app_version_label),
                    context.createSpan {
                        append(semiBold(item.appVersionName))
                        append(" (${item.appVersionCode})")
                    }
                )
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___android_os_label), item.androidOs)
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___android_api_level_label), item.androidAPILevel)
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___orientation_label), item.screenOrientation.capitalizeText())
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(
                    context.getString(R.string.pluto_excep___rooted_label),
                    context.createSpan {
                        append(bold(item.isRooted.toString()))
                    }
                )
            }
        )
    }

    private fun LinearLayoutCompat.setupDeviceInfoUI(item: DeviceInfo) {
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___model_label), "${item.buildBrand?.capitalizeText()} ${item.buildModel}")
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___height_label), "${item.screenHeightPx} px")
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___width_label), "${item.screenWidthPx} px")
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___density_label), "${item.screenDensityDpi} dpi")
            }
        )
        addView(
            KeyValuePairView(context).apply {
                set(context.getString(R.string.pluto_excep___size_label), "${item.screenSizeInch} inches")
            }
        )
    }
}
