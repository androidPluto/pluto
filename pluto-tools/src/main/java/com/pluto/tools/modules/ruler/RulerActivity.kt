package com.pluto.tools.modules.ruler

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.pluto.tools.R
import com.pluto.tools.databinding.PlutoToolRulerActivityBinding
import com.pluto.tools.modules.ruler.internal.ControlsWidget
import com.pluto.tools.modules.ruler.internal.RulerFragment
import com.pluto.tools.modules.ruler.internal.control.ControlCta
import com.pluto.utilities.extensions.toast

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
                ControlCta(ID_CLOSE, R.drawable.pluto_tool___ic_ruler_control_close),
                ControlCta(ID_HINT, R.drawable.pluto_tool___ic_ruler_control_hint),
                ControlCta(ID_MOVE_RIGHT, R.drawable.pluto_tool___ic_ruler_control_move_right)
            ),
            onControlCtaListener
        )

        binding.rightControls.initialise(
            listOf(
                ControlCta(ID_MOVE_LEFT, R.drawable.pluto_tool___ic_ruler_control_move_left),
                ControlCta(ID_HINT, R.drawable.pluto_tool___ic_ruler_control_hint),
                ControlCta(ID_CLOSE, R.drawable.pluto_tool___ic_ruler_control_close)
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
                ID_HINT -> toast("hint clicked")
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
