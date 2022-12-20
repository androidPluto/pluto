package com.pluto.plugins.layoutinspector.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewAttrBinding
import com.pluto.utilities.device.Device
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.sharing.ContentShareViewModel
import com.pluto.utilities.sharing.Shareable
import com.pluto.utilities.sharing.lazyContentSharer
import com.pluto.utilities.viewBinding


class ViewAttrFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoLiFragmentViewAttrBinding::bind)
    private val contentSharer: ContentShareViewModel by lazyContentSharer()
    private var targetView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_li___fragment_view_attr, container, false)

    override fun getTheme(): Int = R.style.PlutoLIBottomSheetDialog

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLifecycle.topActivity?.let {
            findViewByDefaultTag(ViewUtils.tryGetTheFrontView(it))?.let { view ->
                targetView = view
            } ?: run {
                targetView?.setTag(R.id.pluto_li___unique_view_tag, null)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        targetView?.setTag(R.id.pluto_li___unique_view_tag, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        targetView?.let { target ->
            binding.close.setOnDebounceClickListener {
                dismiss()
            }
            binding.share.setOnDebounceClickListener {
                contentSharer.share(Shareable(title = "Sharing View Attributes", content = "view attribute", fileName = "View Attributes generated via Pluto"))
            }
            convertArguments(arguments).apply {

            }
            binding.title.text = target.javaClass.simpleName
        }
    }

    private fun findViewByDefaultTag(root: View): View? {
        if (root.getTag(R.id.pluto_li___unique_view_tag) != null) {
            return root
        }
        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                val view = findViewByDefaultTag(root.getChildAt(i))
                if (view != null) {
                    return view
                }
            }
        }
        return null
    }

    private fun convertArguments(arguments: Bundle?): String? = arguments?.getString("data")
}