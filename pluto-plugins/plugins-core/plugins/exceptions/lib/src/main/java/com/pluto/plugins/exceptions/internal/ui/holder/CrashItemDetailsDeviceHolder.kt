package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsDeviceBinding
import com.pluto.plugins.exceptions.internal.DeviceInfo
import com.pluto.utilities.extensions.capitalizeText
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.spannable.createSpan
import com.pluto.utilities.views.keyvalue.KeyValuePairData

internal class CrashItemDetailsDeviceHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_device), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsDeviceBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is DeviceInfo) {
            setupAppDataTable(item)
            setupDeviceDataTable(item)
        }
    }

    private fun setupAppDataTable(item: DeviceInfo) {
        val dataList = arrayListOf<KeyValuePairData>().apply {
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___app_version_label),
                    value = context.createSpan {
                        append(semiBold(item.appVersionName))
                        append(" (${item.appVersionCode})")
                    }
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___android_os_label),
                    value = item.androidOs
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___android_api_level_label),
                    value = item.androidAPILevel
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___orientation_label),
                    value = item.screenOrientation.capitalizeText()
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___rooted_label),
                    value = context.createSpan {
                        append(bold(item.isRooted.toString()))
                    }
                )
            )
        }
        binding.appDataTable.set(
            title = context.getString(R.string.pluto_excep___app_state_label),
            keyValuePairs = dataList
        )
    }

    private fun setupDeviceDataTable(item: DeviceInfo) {
        val dataList = arrayListOf<KeyValuePairData>().apply {
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___app_version_label),
                    value = "${item.buildBrand?.capitalizeText()} ${item.buildModel}"
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___height_label),
                    value = "${item.screenHeightPx} px"
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___width_label),
                    value = "${item.screenWidthPx} px"
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___density_label),
                    value = "${item.screenDensityDpi} dpi"
                )
            )
            add(
                KeyValuePairData(
                    key = context.getString(R.string.pluto_excep___size_label),
                    value = "${item.screenSizeInch} inches"
                )
            )
        }
        binding.deviceDataTable.set(
            title = context.getString(R.string.pluto_excep___device_label),
            keyValuePairs = dataList
        )
    }
}
