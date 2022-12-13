package com.pluto.plugins.layoutinspector

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewInfoBinding
import com.pluto.plugins.layoutinspector.internal.ActivityLifecycle
import com.pluto.plugins.layoutinspector.internal.control.ControlCta
import com.pluto.plugins.layoutinspector.internal.control.ControlsWidget
import com.pluto.plugins.layoutinspector.internal.hint.HintFragment
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.viewBinding

internal class ViewInfoFragment : Fragment(R.layout.pluto_li___fragment_view_info), View.OnClickListener {

    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private var targetView: View? = null
    private val binding by viewBinding(PlutoLiFragmentViewInfoBinding::bind)
//    private val activityLifecycle = ActivityLifecycle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityLifecycle.topActivity?.let { binding.operableView.tryGetFrontView(it) }
        binding.operableView.setOnClickListener(this)
        binding.leftControls.initialise(
            listOf(
                ControlCta(ID_CLOSE, R.drawable.pluto_li___ic_control_close),
                ControlCta(ID_HINT, R.drawable.pluto_li___ic_control_hint),
                ControlCta(ID_MOVE_RIGHT, R.drawable.pluto_li___ic_control_move_right)
            ),
            onControlCtaListener
        )

        binding.rightControls.initialise(
            listOf(
                ControlCta(ID_MOVE_LEFT, R.drawable.pluto_li___ic_control_move_left),
                ControlCta(ID_HINT, R.drawable.pluto_li___ic_control_hint),
                ControlCta(ID_CLOSE, R.drawable.pluto_li___ic_control_close)
            ),
            onControlCtaListener
        )
        binding.leftControls.visibility = View.GONE
        setupPreviewPanel()

//        binding.paramPanelContainer.setBehavior(behavior = BottomSheetBehavior())
    }

    private fun setupPreviewPanel() {
        behavior = BottomSheetBehavior.from(binding.bsContainer.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(view: View) {
        if (binding.operableView.isSelectedEmpty()) {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        targetView = view
        refreshViewDetails(view)
    }

    private fun refreshViewDetails(view: View) {
        context?.toast("${view.width}, ${view.height} : ${view.javaClass.simpleName}")
//        binding.text.text = "${view.width}, ${view.height}"
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
