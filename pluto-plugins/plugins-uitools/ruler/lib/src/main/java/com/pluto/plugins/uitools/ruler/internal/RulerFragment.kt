package com.pluto.plugins.uitools.ruler.internal

import android.view.View
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugins.uitools.FullScreenTool

internal class RulerFragment : FullScreenTool() {

    override fun getToolView(): View {
        val view = RulerScaleView(requireContext())
        view.isClickable = true
        return view
    }

    override fun onHintClicked() {
        requireContext().toast("hint clicked")
    }

    override fun onCloseClicked() {
    }
}
