package com.mocklets.pluto.modules.exceptions.ui.holder

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DeviceFingerPrint
import com.mocklets.pluto.core.extensions.capitalizeText
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.databinding.PlutoItemCrashDetailsDeviceBinding

internal class CrashItemDetailsDeviceHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_crash_details_device), actionListener) {

    private val binding = PlutoItemCrashDetailsDeviceBinding.bind(itemView)

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
        if (item is DeviceFingerPrint) {
            item.software.appVersion?.let { appVersion.text = "${it.name} (${it.code})" }
            androidOS.text = item.software.androidOs
            androidAPI.text = item.software.androidAPILevel
            orientation.text = item.software.orientation.capitalizeText()
            rooted.text = item.isRooted.toString()

            build.text = "${item.build.brand?.capitalizeText()} ${item.build.model}"
            height.text = item.screen.height
            width.text = item.screen.width
            density.text = item.screen.density
            size.text = item.screen.size
        }
    }
}
