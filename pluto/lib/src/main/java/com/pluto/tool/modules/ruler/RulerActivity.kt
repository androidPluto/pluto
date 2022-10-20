package com.pluto.tool.modules.ruler

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.pluto.R
import com.pluto.databinding.PlutoToolRulerActivityBinding
import com.pluto.tool.modules.ruler.internal.ControlsWidget
import com.pluto.tool.modules.ruler.internal.RulerFragment
import com.pluto.tool.modules.ruler.internal.control.ControlCta
import com.pluto.tool.modules.ruler.internal.hint.HintFragment

class RulerActivity : AppCompatActivity() {

    private lateinit var binding: PlutoToolRulerActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlutoToolRulerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.container, RulerFragment()).commit()
        }

        binding.leftControls.initialise(
            listOf(
                ControlCta(ID_CLOSE, R.drawable.pluto___tool_ic_ruler_control_close),
                ControlCta(ID_HINT, R.drawable.pluto___tool_ic_ruler_control_hint),
                ControlCta(ID_MOVE_RIGHT, R.drawable.pluto___tool_ic_ruler_control_move_right)
            ),
            onControlCtaListener
        )

        binding.rightControls.initialise(
            listOf(
                ControlCta(ID_MOVE_LEFT, R.drawable.pluto___tool_ic_ruler_control_move_left),
                ControlCta(ID_HINT, R.drawable.pluto___tool_ic_ruler_control_hint),
                ControlCta(ID_CLOSE, R.drawable.pluto___tool_ic_ruler_control_close)
            ),
            onControlCtaListener
        )
        binding.leftControls.visibility = GONE
    }

    private val onControlCtaListener = object : ControlsWidget.OnClickListener {
        override fun onClick(id: String) {
            when (id) {
                ID_MOVE_RIGHT -> {
                    binding.leftControls.visibility = GONE
                    binding.rightControls.visibility = VISIBLE
                }

                ID_MOVE_LEFT -> {
                    binding.leftControls.visibility = VISIBLE
                    binding.rightControls.visibility = GONE
                }

                ID_CLOSE -> finish()
                ID_HINT -> HintFragment().show(supportFragmentManager, "hint")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private companion object {
        const val ID_CLOSE = "close"
        const val ID_HINT = "hint"
        const val ID_MOVE_LEFT = "moveToLeft"
        const val ID_MOVE_RIGHT = "moveToRight"
    }
}
