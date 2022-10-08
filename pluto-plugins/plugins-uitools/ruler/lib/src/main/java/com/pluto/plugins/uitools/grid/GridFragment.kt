package com.pluto.plugins.uitools.grid

import com.pluto.plugins.uitools.FullScreenTool
import com.pluto.utilities.extensions.toast

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
