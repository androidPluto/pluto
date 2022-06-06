package com.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sampleapp.databinding.FragmentContainerBinding

class ContainerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentContainerBinding? = null
    private val binding
        get() = _binding!!

    override fun getTheme(): Int = R.style.DemoBottomSheetDialog
    private val functionInfo: FunctionsModel
        get() = FunctionsModel(id = requireArguments().getString(FUNCTION_ID)!!, label = requireArguments().getString(FUNCTION_LABEL)!!)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = requireContext().getScreen().second
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        addFunctionFragment()
        binding.title.text = functionInfo.label
    }

    private fun addFunctionFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.container, SupportedPlugins.getDemoFragment(functionInfo.id))
            .commit()
    }

    companion object {
        const val FUNCTION_ID = "functionId"
        const val FUNCTION_LABEL = "functionLabel"
    }
}
