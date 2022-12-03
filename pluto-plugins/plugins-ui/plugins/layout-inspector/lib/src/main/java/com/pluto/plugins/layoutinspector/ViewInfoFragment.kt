package com.pluto.plugins.layoutinspector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewInfoBinding
import com.pluto.utilities.DebugLog
import com.pluto.utilities.viewBinding

internal class ViewInfoFragment : Fragment(R.layout.pluto_li___fragment_view_info), View.OnClickListener {

    private var targetView: View? = null
    private val binding by viewBinding(PlutoLiFragmentViewInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.operableView.tryGetFrontView(Pandora.get().getTopActivity())
        binding.operableView.setOnClickListener(this)

//        binding.paramPanelContainer.setBehavior(behavior = BottomSheetBehavior())
    }
    override fun onClick(view: View) {
        if (binding.operableView.isSelectedEmpty()) {
            DebugLog.e("VIF", "ov empty")
//            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
//            if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
//                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
        }
        targetView = view;
        refreshViewDetails(view);
    }

    private fun refreshViewDetails(view: View) {
//        binding.text.text = "${view.width}, ${view.height}"
    }

}
