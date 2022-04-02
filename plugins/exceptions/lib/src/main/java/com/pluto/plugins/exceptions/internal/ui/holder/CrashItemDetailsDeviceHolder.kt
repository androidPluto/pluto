package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.capitalizeText
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsDeviceBinding
import com.pluto.plugins.exceptions.internal.DeviceInfo

internal class CrashItemDetailsDeviceHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_device), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsDeviceBinding.bind(itemView)

    private val appVersion = binding.appVersion
    private val androidOS = binding.androidOS
    private val androidAPI = binding.androidAPILevel
    private val orientation = binding.orientation
    private val rooted = binding.rooted

    private val height = binding.deviceHeight
    private val width = binding.deviceWidth
    private val density = binding.density
    private val size = binding.size
    private val build = binding.build

    override fun onBind(item: ListItem) {
        if (item is DeviceInfo) {
            item.appVersionName.let { appVersion.text = "$it (${item.appVersionCode})" }
            androidOS.text = item.androidOs
            androidAPI.text = item.androidAPILevel
            orientation.text = item.screenOrientation.capitalizeText()
            rooted.text = item.isRooted.toString()

            build.text = "${item.buildBrand?.capitalizeText()} ${item.buildModel}"
            height.text = "${item.screenHeightPx} px"
            width.text = "${item.screenWidthPx} px"
            density.text = "${item.screenDensityDpi} dpi"
            size.text = "${item.screenSizeInch} inches"
        }
    }
}
