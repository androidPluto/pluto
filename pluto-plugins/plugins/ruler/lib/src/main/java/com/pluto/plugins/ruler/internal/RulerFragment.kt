package com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pluto.plugins.ruler.R
import com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.control.ControlCta
import com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.hint.HintFragment
import com.pluto.plugins.ruler.databinding.PlutoRulerRulerFragmentBinding
import com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.ControlsWidget
import com.pluto.utilities.viewBinding

internal class RulerFragment : Fragment(R.layout.pluto_ruler___ruler_fragment) {

    private val binding by viewBinding(PlutoRulerRulerFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.leftControls.initialise(
            listOf(
                ControlCta(ID_CLOSE, R.drawable.pluto_ruler___ic_control_close),
                ControlCta(ID_HINT, R.drawable.pluto_ruler___ic_control_hint),
                ControlCta(ID_MOVE_RIGHT, R.drawable.pluto_ruler___ic_control_move_right)
            ),
            onControlCtaListener
        )

        binding.rightControls.initialise(
            listOf(
                ControlCta(ID_MOVE_LEFT, R.drawable.pluto_ruler___ic_control_move_left),
                ControlCta(ID_HINT, R.drawable.pluto_ruler___ic_control_hint),
                ControlCta(ID_CLOSE, R.drawable.pluto_ruler___ic_control_close)
            ),
            onControlCtaListener
        )
        binding.leftControls.visibility = View.GONE
    }

    private val onControlCtaListener = object : ControlsWidget.OnClickListener {
        override fun onClick(id: String) {
            when (id) {
                ID_MOVE_RIGHT -> {
                    binding.leftControls.visibility = View.GONE
                    binding.rightControls.visibility = View.VISIBLE
                }

                ID_MOVE_LEFT -> {
                    binding.leftControls.visibility = View.VISIBLE
                    binding.rightControls.visibility = View.GONE
                }

                ID_CLOSE -> requireActivity().finish()
                ID_HINT -> HintFragment().show(requireActivity().supportFragmentManager, "hint")
            }
        }
    }

    private companion object {
        const val ID_CLOSE = "close"
        const val ID_HINT = "hint"
        const val ID_MOVE_LEFT = "moveToLeft"
        const val ID_MOVE_RIGHT = "moveToRight"
    }
}
