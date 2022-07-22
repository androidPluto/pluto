package com.pluto.plugins.network.internal.interceptor.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.asFormattedDate
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentDetailsOverviewBinding
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.DetailContentData
import com.pluto.plugins.network.internal.interceptor.logic.ExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkViewModel
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData
import com.pluto.plugins.network.internal.interceptor.logic.formatSizeAsBytes
import com.pluto.plugins.network.internal.mock.logic.MockSettingsViewModel
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity

internal class OverviewFragment : Fragment(R.layout.pluto_network___fragment_details_overview) {

    private val binding by viewBinding(PlutoNetworkFragmentDetailsOverviewBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()
    private val mockSettingsViewModel: MockSettingsViewModel by viewModels()
    private val contentSharer by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.detailContentLiveData.removeObservers(viewLifecycleOwner)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, apiCallObserver)

        mockSettingsViewModel.currentMock.removeObservers(viewLifecycleOwner)
        mockSettingsViewModel.currentMock.observe(viewLifecycleOwner, proxySettingsObserver)

        binding.settingStub.setupMock.setOnDebounceClickListener {
            viewModel.detailContentLiveData.value?.api?.request?.let { request ->
                val bundle = bundleOf("url" to request.url.toString(), "method" to request.method)
                findNavController().navigate(R.id.openMockSettingsEdit, bundle)
            }
        }
    }

    private val apiCallObserver = Observer<DetailContentData> {
        updateUi(it.api, it.search)
        mockSettingsViewModel.fetch(it.api.request.url.toString(), it.api.request.method)
    }

    private val proxySettingsObserver = Observer<MockSettingsEntity?> { mock ->
        context?.apply {
            binding.settingStub.setupMock.text =
                getString(if (mock != null) R.string.pluto_network___update_api_proxy else R.string.pluto_network___setup_api_proxy)
        }
    }

    private fun updateUi(data: ApiCallData, search: String?) {
        setupStatusView(data)
        binding.url.setSpan { append(highlight(data.request.url.toString(), search)) }
        setupProxyState(data)
        binding.method.setSpan { append(highlight(data.request.method, search)) }
        binding.ssl.setSpan { append(highlight(data.request.url.isHttps.toString(), search)) }
        binding.requestTime.setSpan {
            append(semiBold(data.request.timestamp.asFormattedDate(DATE_FORMAT)))
        }
        binding.requestSize.setSpan {
            append(semiBold(formatSizeAsBytes(data.request.body?.body?.length?.toLong() ?: 0L)))
        }
        binding.settingStub.copyCurl.setOnDebounceClickListener {
            contentSharer.share(Shareable(title = "Share Request cURL", content = data.curl, fileName = "cURL Request from Pluto"))
        }
        data.exception?.let {
            handleExceptionUI(it, data)
        }
        data.response?.let {
            handleResponseUI(it)
        }
    }

    private fun handleResponseUI(it: ResponseData) {
        binding.protocol.setSpan {
            append(semiBold(fontColor("${it.protocol}", context.color(R.color.pluto___text_dark_80))))
            append(regular(fontColor(" (${it.protocol.name})", context.color(R.color.pluto___text_dark_60))))
        }
        binding.requestTime.setSpan {
            append(semiBold(it.sendTimeMillis.asFormattedDate(DATE_FORMAT)))
        }
        binding.responseTime.setSpan {
            append(semiBold(it.receiveTimeMillis.asFormattedDate(DATE_FORMAT)))
        }
        binding.delay.setSpan {
            append(semiBold("${it.receiveTimeMillis - it.sendTimeMillis} ms"))
        }
        binding.responseSize.setSpan {
            append(semiBold(formatSizeAsBytes(it.body?.body?.length?.toLong() ?: 0L)))
        }
    }

    private fun handleExceptionUI(it: ExceptionData, data: ApiCallData) {
        binding.protocol.setSpan {
            append(semiBold(context.getString(R.string.pluto_network___na)))
        }
        binding.responseTime.setSpan {
            append(semiBold(it.timeStamp.asFormattedDate(DATE_FORMAT)))
        }
        binding.delay.setSpan {
            append(semiBold("${it.timeStamp - data.request.timestamp} ms"))
        }
    }

    private fun setupProxyState(data: ApiCallData) {
        if (data.mock != null) {
            binding.settingStub.mockUrlGroup.visibility = VISIBLE
            binding.settingStub.settingsUrl.text = data.mock?.url
        } else {
            binding.settingStub.mockUrlGroup.visibility = GONE
        }
    }

    private fun setupStatusView(data: ApiCallData) {
        binding.progress.visibility = VISIBLE
        binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.status.text = context?.getString(R.string.pluto_network___network_state_in_progress)
        binding.statusView.setBackgroundColor(requireContext().color(R.color.pluto___dark_05))

        data.exception?.let {
            binding.progress.visibility = GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_network___ic_error, 0, 0, 0)
            binding.status.setSpan {
                append(fontColor(it.name ?: context.getString(R.string.pluto_network___network_state_failed), context.color(R.color.pluto___red)))
            }
            binding.statusView.setBackgroundColor(requireContext().color(R.color.pluto___red_05))
        }

        data.response?.let {
            binding.progress.visibility = GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(
                if (it.isSuccessful) R.drawable.pluto_network___ic_success else R.drawable.pluto_network___ic_error, 0, 0, 0
            )
            binding.status.setSpan {
                append(
                    fontColor(
                        bold("${it.status.code} "),
                        context.color(if (it.isSuccessful) R.color.pluto___dull_green else R.color.pluto___red)
                    )
                )
                append(
                    fontColor(
                        it.status.message,
                        context.color(if (it.isSuccessful) R.color.pluto___dull_green else R.color.pluto___red)
                    )
                )
            }
            binding.statusView.setBackgroundColor(requireContext().color(if (it.isSuccessful) R.color.pluto___dull_green_08 else R.color.pluto___red_05))
        }
    }

    private companion object {
        const val DATE_FORMAT = "MMM dd, yyyy, HH:mm:ss.SSS"
    }
}
