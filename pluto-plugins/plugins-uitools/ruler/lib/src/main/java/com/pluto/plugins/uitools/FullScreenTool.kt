package com.pluto.plugins.uitools

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.pluto.plugins.uitools.ruler.internal.ControlsWidget

internal abstract class FullScreenTool : Fragment() {

    abstract fun getToolView(): View
    abstract fun onHintClicked()
    abstract fun onCloseClicked()

    private lateinit var parent: ConstraintLayout
    private lateinit var controls: ControlsWidget

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        parent = ConstraintLayout(requireContext())
        controls = generateControls(requireContext())
        return installControls(getToolView().apply { id = View.generateViewId() })
    }

    private fun generateControls(context: Context): ControlsWidget {
        return ControlsWidget(context).apply {
            id = View.generateViewId()
            initialise(onControlInteractionListener)
        }
    }

    private fun installControls(view: View): View {
        val rlParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        parent.addView(view, rlParams)
        parent.addView(controls)
        positionControls(true)
        return parent
    }

    private fun positionControls(isRightAligned: Boolean) {
        clearConstraints()
        val set = ConstraintSet()
        set.clone(parent)
        val constraintAlignment = if (isRightAligned) ConstraintSet.RIGHT else ConstraintSet.LEFT
        set.connect(controls.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, CONTROL_TOP_MARGIN)
        set.connect(controls.id, constraintAlignment, ConstraintSet.PARENT_ID, constraintAlignment, CONTROL_HORIZONTAL_MARGIN)
        set.applyTo(parent)
    }

    private fun clearConstraints() {
        val set = ConstraintSet()
        set.clone(parent)
        set.clear(controls.id, ConstraintSet.RIGHT)
        set.clear(controls.id, ConstraintSet.LEFT)
        set.applyTo(parent)
    }

    private val onControlInteractionListener = object : OnControlClickListener {
        override fun onHint() {
            onHintClicked()
        }

        override fun onClose() {
            onCloseClicked()
        }
    }

    interface OnControlClickListener {
        fun onHint()
        fun onClose()
    }

    private companion object {
        const val CONTROL_TOP_MARGIN = 60
        const val CONTROL_HORIZONTAL_MARGIN = 60
    }
}
