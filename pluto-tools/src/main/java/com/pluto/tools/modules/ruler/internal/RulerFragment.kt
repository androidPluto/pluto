package com.pluto.tools.modules.ruler.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

internal class RulerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return RulerScaleView(requireContext()).apply {
            id = View.generateViewId()
        }
    }

//    override fun getToolView(): View {
//        val view = RulerScaleView(requireContext())
//        view.isClickable = true
//        return view
//    }
//
//    override fun onHintClicked() {
//        requireContext().toast("hint clicked")
//    }
//
//    override fun onCloseClicked() {
//    }
}
