package com.pluto.plugins.uitools.grid

import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugins.uitools.FullScreenTool

internal class GridFragment : FullScreenTool() {

    override fun getToolView() = GridView(requireContext()).apply {
        isClickable = false
    }

    override fun onHintClicked() {
        requireContext().toast("hint clicked")
    }

    override fun onCloseClicked() {
    }
}
