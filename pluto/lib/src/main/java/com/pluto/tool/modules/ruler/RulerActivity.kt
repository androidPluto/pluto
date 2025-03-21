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

/**
 * Activity that displays the ruler interface for measuring UI elements.
 *
 * This activity hosts the ruler view and control widgets that allow the user to
 * measure distances and sizes on the screen. It provides controls for showing hints,
 * closing the ruler, and moving the control panel between the left and right sides
 * of the screen for easier one-handed operation.
 */
class RulerActivity : AppCompatActivity() {

    private lateinit var binding: PlutoToolRulerActivityBinding

    /**
     * Initializes the activity, sets up the ruler view and control widgets.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlutoToolRulerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add the ruler fragment to the container
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.container, RulerFragment()).commit()
        }

        // Initialize the left control panel with close, hint, and move right buttons
        binding.leftControls.initialise(
            listOf(
                ControlCta(ID_CLOSE, R.drawable.pluto___tool_ic_ruler_control_close),
                ControlCta(ID_HINT, R.drawable.pluto___tool_ic_ruler_control_hint),
                ControlCta(ID_MOVE_RIGHT, R.drawable.pluto___tool_ic_ruler_control_move_right)
            ),
            onControlCtaListener
        )

        // Initialize the right control panel with move left, hint, and close buttons
        binding.rightControls.initialise(
            listOf(
                ControlCta(ID_MOVE_LEFT, R.drawable.pluto___tool_ic_ruler_control_move_left),
                ControlCta(ID_HINT, R.drawable.pluto___tool_ic_ruler_control_hint),
                ControlCta(ID_CLOSE, R.drawable.pluto___tool_ic_ruler_control_close)
            ),
            onControlCtaListener
        )

        // Start with the left controls hidden (right controls visible)
        binding.leftControls.visibility = GONE
    }

    /**
     * Listener for control button clicks that handles the various control actions.
     */
    private val onControlCtaListener = object : ControlsWidget.OnClickListener {
        override fun onClick(id: String) {
            when (id) {
                ID_MOVE_RIGHT -> {
                    // Move controls to the right side
                    binding.leftControls.visibility = GONE
                    binding.rightControls.visibility = VISIBLE
                }

                ID_MOVE_LEFT -> {
                    // Move controls to the left side
                    binding.leftControls.visibility = VISIBLE
                    binding.rightControls.visibility = GONE
                }

                ID_CLOSE -> finish() // Close the ruler activity
                ID_HINT -> HintFragment().show(supportFragmentManager, "hint") // Show the hint dialog
            }
        }
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Finishes the activity to ensure it doesn't remain in the background.
     */
    override fun onStop() {
        super.onStop()
        finish()
    }

    private companion object {
        /** ID for the close button */
        const val ID_CLOSE = "close"

        /** ID for the hint button */
        const val ID_HINT = "hint"

        /** ID for the move left button */
        const val ID_MOVE_LEFT = "moveToLeft"

        /** ID for the move right button */
        const val ID_MOVE_RIGHT = "moveToRight"
    }
}
