package com.pluto.plugins.layoutinspector

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewInfoBinding
import com.pluto.plugins.layoutinspector.internal.ActivityLifecycle
import com.pluto.plugins.layoutinspector.internal.control.ControlCta
import com.pluto.plugins.layoutinspector.internal.control.ControlsWidget
import com.pluto.plugins.layoutinspector.internal.hierarchy.ViewHierarchyFragment.Companion.SCROLL_TO_TARGET
import com.pluto.plugins.layoutinspector.internal.hint.HintFragment
import com.pluto.plugins.layoutinspector.internal.inspect.InspectViewModel
import com.pluto.plugins.layoutinspector.internal.inspect.assignTargetTag
import com.pluto.plugins.layoutinspector.internal.inspect.clearTargetTag
import com.pluto.utilities.viewBinding

internal class ViewInfoFragment : Fragment(R.layout.pluto_li___fragment_view_info), View.OnClickListener {

    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private var targetView: View? = null
    private val binding by viewBinding(PlutoLiFragmentViewInfoBinding::bind)
    private val inspectViewModel: InspectViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.bsContainer.previewPanelBottomSheet.setPadding(0, 0, 0, systemBarsInsets.bottom)
            insets
        }
        ActivityLifecycle.topActivity?.let { binding.operableView.tryGetFrontView(it) }
        binding.operableView.setOnClickListener(this)
        binding.leftControls.initialise(
            listOf(
                ControlCta(ID_CLOSE, R.drawable.pluto_li___ic_control_close, getString(R.string.pluto_li___cntrl_title_close)),
                ControlCta(ID_HINT, R.drawable.pluto_li___ic_control_hint, getString(R.string.pluto_li___cntrl_title_hint)),
                ControlCta(ID_HIERARCHY, R.drawable.pluto_li___ic_view_hierarchy, getString(R.string.pluto_li___cntrl_title_hierarchy)),
                ControlCta(ID_MOVE_RIGHT, R.drawable.pluto_li___ic_control_move_right, getString(R.string.pluto_li___cntrl_title_move_to_right))
            ),
            onControlCtaListener
        )

        binding.rightControls.initialise(
            listOf(
                ControlCta(ID_MOVE_LEFT, R.drawable.pluto_li___ic_control_move_left, getString(R.string.pluto_li___cntrl_title_move_to_left)),
                ControlCta(ID_HIERARCHY, R.drawable.pluto_li___ic_view_hierarchy, getString(R.string.pluto_li___cntrl_title_hierarchy)),
                ControlCta(ID_HINT, R.drawable.pluto_li___ic_control_hint, getString(R.string.pluto_li___cntrl_title_hint)),
                ControlCta(ID_CLOSE, R.drawable.pluto_li___ic_control_close, getString(R.string.pluto_li___cntrl_title_close))
            ),
            onControlCtaListener
        )
        binding.leftControls.visibility = View.GONE
        setupPreviewPanel()

        inspectViewModel.view.removeObserver(inspectRequestObserver)
        inspectViewModel.view.observe(viewLifecycleOwner, inspectRequestObserver)
    }

    private fun setupPreviewPanel() {
        behavior = BottomSheetBehavior.from(binding.bsContainer.previewPanelBottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(view: View) {
        clearPreviousSelection()
        if (binding.operableView.isSelectedEmpty()) {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            targetView = view
            targetView?.assignTargetTag()
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            refreshViewDetails(view)
        }
    }

    private fun clearPreviousSelection() {
        targetView?.clearTargetTag()
        targetView = null
    }

    private fun refreshViewDetails(view: View) {
        binding.bsContainer.previewPanel.refresh(
            view = view,
            onViewAttrRequested = {
                findNavController().navigate(R.id.openAttrView)
            },
            onViewHierarchyRequested = {
                val bundle = bundleOf(SCROLL_TO_TARGET to true)
                findNavController().navigate(R.id.openHierarchyView, bundle)
            },
            onCloseRequested = {
                clearPreviousSelection()
                binding.operableView.handleClick(view, true)
            }
        )
    }

    private val inspectRequestObserver = Observer<View> {
        binding.operableView.handleClick(it, false)
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

                ID_HIERARCHY -> findNavController().navigate(R.id.openHierarchyView)
                ID_CLOSE -> requireActivity().finish()
                ID_HINT -> HintFragment().show(requireActivity().supportFragmentManager, "hint")
            }
        }
    }

    private companion object {
        const val ID_CLOSE = "close"
        const val ID_HINT = "hint"
        const val ID_HIERARCHY = "hierarchy"
        const val ID_MOVE_LEFT = "moveToLeft"
        const val ID_MOVE_RIGHT = "moveToRight"
    }
}
