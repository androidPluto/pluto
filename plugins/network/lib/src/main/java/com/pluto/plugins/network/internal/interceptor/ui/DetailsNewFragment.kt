package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentDetailsNewBinding
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.DetailContentData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkViewModel

class DetailsNewFragment : Fragment(R.layout.pluto_network___fragment_details_new) {

    private val binding by viewBinding(PlutoNetworkFragmentDetailsNewBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        setupControls()

        viewModel.detailContentLiveData.removeObserver(detailsObserver)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, detailsObserver)

        viewModel.apiCalls.removeObserver(listUpdateObserver)
        viewModel.apiCalls.observe(viewLifecycleOwner, listUpdateObserver)
    }

    private fun setupControls() {
        binding.close.setOnDebounceClickListener {
            requireActivity().onBackPressed()
        }
        binding.share.setOnDebounceClickListener {
            findNavController().navigate(R.id.openShareView)
        }
    }

    private val detailsObserver = Observer<DetailContentData> {
        binding.title.setSpan {
            append(fontColor(bold("${it.api.request.method.uppercase()},\t"), requireContext().color(R.color.pluto___white_60)))
            append(it.api.request.url.encodedPath)
        }
        binding.overview.set(it.api)
        binding.request.set(it.api.request)
        binding.response.set(it.api.response, it.api.exception)
    }

    private val listUpdateObserver = Observer<List<ApiCallData>> {
        val id = requireArguments().getString(API_CALL_ID, null)
        if (!id.isNullOrEmpty()) {
            viewModel.setCurrent(id)
        } else {
            requireContext().toast("invalid id")
            requireActivity().onBackPressed()
        }
    }

    companion object {
        internal const val API_CALL_ID = "id"
    }
}
