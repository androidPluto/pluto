package com.pluto.tools.modules.ruler.internal

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.tools.FullScreenTool
import com.pluto.tools.databinding.PlutoToolRulerControlsBinding
import com.pluto.utilities.setOnDebounceClickListener

internal class ControlsWidget(context: Context) : ConstraintLayout(context) {

    private val binding = PlutoToolRulerControlsBinding.inflate(LayoutInflater.from(context), this, true)

    fun initialise(listener: FullScreenTool.OnControlClickListener? = null) {
        binding.text.setOnDebounceClickListener(haptic = true) {
            listener?.onHint()
        }
    }
}
