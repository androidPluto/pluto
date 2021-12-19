package com.mocklets.pluto.logger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mocklets.pluto.logger.LogData
import com.mocklets.pluto.logger.LogsViewModel
import com.mocklets.pluto.logger.R
import com.mocklets.pluto.logger.databinding.PlutoLoggerFragmentDetailsBinding
import com.mocklets.pluto.utilities.extensions.toast
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding

internal class DetailsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoLoggerFragmentDetailsBinding::bind)
    private val viewModel: LogsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_logger___fragment_details, container, false)

    override fun getTheme(): Int = R.style.PlutoLoggerBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        binding.text.setDebounceClickListener {
            findNavController().popBackStack()
        }

        viewModel.current.removeObserver(detailsObserver)
        viewModel.current.observe(viewLifecycleOwner, detailsObserver)
    }

    private val detailsObserver = Observer<LogData> {
        context?.toast(it.message)
    }
}
