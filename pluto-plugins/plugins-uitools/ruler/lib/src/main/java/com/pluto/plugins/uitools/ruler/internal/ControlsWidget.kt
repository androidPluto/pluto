package com.pluto.plugins.uitools.ruler.internal

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugins.uitools.FullScreenTool
import com.pluto.plugins.uitools.databinding.PlutoRulerControlsBinding

internal class ControlsWidget(context: Context) : ConstraintLayout(context) {

    private val binding = PlutoRulerControlsBinding.inflate(LayoutInflater.from(context), this, true)

    fun initialise(listener: FullScreenTool.OnControlClickListener? = null) {
        binding.text.setOnDebounceClickListener(haptic = true) {
            listener?.onHint()
        }
    }
}
