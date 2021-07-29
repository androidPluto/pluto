package com.mocklets.pluto.modules.network.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.asFormattedDate
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.copyToClipboard
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.ui.routing.Screens
import com.mocklets.pluto.core.ui.routing.lazyRouter
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.createSpan
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoFragmentNetworkDetailsOverviewBinding
import com.mocklets.pluto.modules.exceptions.ExceptionData
import com.mocklets.pluto.modules.network.ApiCallData
import com.mocklets.pluto.modules.network.ResponseData
import com.mocklets.pluto.modules.network.proxy.NetworkProxyViewModel
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsFragment
import com.mocklets.pluto.modules.network.ui.DetailContentData
import com.mocklets.pluto.modules.network.ui.NetworkViewModel

internal class NetworkDetailsOverviewFragment : Fragment(R.layout.pluto___fragment_network_details_overview) {

    private val binding by viewBinding(PlutoFragmentNetworkDetailsOverviewBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()
    private val proxyViewModel: NetworkProxyViewModel by viewModels()
    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.detailContentLiveData.removeObservers(viewLifecycleOwner)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, apiCallObserver)

        proxyViewModel.currentProxy.removeObservers(viewLifecycleOwner)
        proxyViewModel.currentProxy.observe(viewLifecycleOwner, proxySettingsObserver)

        binding.proxyStub.setupProxy.setDebounceClickListener {
            viewModel.detailContentLiveData.value?.api?.request?.let { request ->
                router.navigate(
                    Screens.NetworkProxySettings(
                        NetworkProxySettingsFragment.Data(
                            request.url.toString(),
                            request.method,
                            true
                        )
                    )
                )
            }
        }
    }

    private val apiCallObserver = Observer<DetailContentData> {
        updateUi(it.api, it.search)
        proxyViewModel.fetch(it.api.request.url.toString(), it.api.request.method)
    }

    private val proxySettingsObserver = Observer<NetworkProxyEntity?> { proxy ->
        context?.apply {
            binding.proxyStub.setupProxy.text =
                getString(if (proxy != null) R.string.pluto___update_api_proxy else R.string.pluto___setup_api_proxy)
        }
    }

    private fun updateUi(data: ApiCallData, search: String?) {
        setupStatusView(data)
        binding.url.setSpan { append(highlight(data.request.url.toString(), search)) }
        setupProxyState(data)
        binding.method.setSpan { append(highlight(data.request.method, search)) }
        binding.ssl.setSpan { append(highlight(data.request.url.isHttps.toString(), search)) }
        binding.requestTime.setSpan {
            append(
                highlight(
                    semiBold(
                        fontColor(
                            data.request.timestamp.asFormattedDate(DATE_FORMAT),
                            context.color(R.color.pluto___text_dark_80)
                        )
                    ),
                    search
                )
            )
        }
        binding.proxyStub.copyCurl.setDebounceClickListener {
            context?.let {
                it.copyToClipboard("curl_code", data.curl)
                it.toast(it.getString(R.string.pluto___curl_copied))
            }
        }

        data.exception?.let {
            handleExceptionUI(it, data, search)
        }

        data.response?.let {
            handleResponseUI(it, search)
        }
    }

    private fun handleResponseUI(it: ResponseData, search: String?) {
        binding.protocol.setSpan {
            val protocolSpan = context.createSpan {
                append(semiBold(fontColor("${it.protocol}", context.color(R.color.pluto___text_dark_80))))
                append(regular(fontColor(" (${it.protocol.name})", context.color(R.color.pluto___text_dark_60))))
            }
            append(highlight(protocolSpan, search))
        }
        binding.requestTime.setSpan {
            append(
                highlight(
                    semiBold(
                        fontColor(
                            it.sendTimeMillis.asFormattedDate(DATE_FORMAT),
                            context.color(R.color.pluto___text_dark_80)
                        )
                    ),
                    search
                )
            )
        }
        binding.responseTime.setSpan {
            append(
                highlight(
                    semiBold(
                        fontColor(
                            it.receiveTimeMillis.asFormattedDate(DATE_FORMAT),
                            context.color(R.color.pluto___text_dark_80)
                        )
                    ),
                    search
                )
            )
        }
        binding.delay.setSpan {
            append(
                highlight(
                    semiBold(
                        fontColor(
                            "${it.receiveTimeMillis - it.sendTimeMillis} ms",
                            context.color(R.color.pluto___text_dark_80)
                        )
                    ),
                    search
                )
            )
        }
    }

    private fun handleExceptionUI(it: ExceptionData, data: ApiCallData, search: String?) {
        binding.protocol.text = context?.getString(R.string.pluto___na)
        binding.responseTime.setSpan {
            append(
                highlight(
                    semiBold(
                        fontColor(
                            it.timeStamp.asFormattedDate(DATE_FORMAT),
                            context.color(R.color.pluto___text_dark_80)
                        )
                    ),
                    search
                )
            )
        }
        binding.delay.setSpan {
            append(
                highlight(
                    semiBold(
                        fontColor(
                            "${it.timeStamp - data.request.timestamp} ms",
                            context.color(R.color.pluto___text_dark_80)
                        )
                    ),
                    search
                )
            )
        }
    }

    private fun setupProxyState(data: ApiCallData) {
        if (data.proxy != null) {
            binding.proxyStub.proxyUrlGroup.visibility = VISIBLE
            binding.proxyStub.settingsUrl.text = data.proxy?.url
        } else {
            binding.proxyStub.proxyUrlGroup.visibility = GONE
        }
    }

    private fun setupStatusView(data: ApiCallData) {
        binding.progress.visibility = VISIBLE
        binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.status.text = context?.getString(R.string.pluto___network_state_in_progress)
        binding.statusView.setBackgroundColor(requireContext().color(R.color.pluto___dark_05))

        data.exception?.let {
            binding.progress.visibility = GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto___ic_error, 0, 0, 0)
            binding.status.setSpan {
                append(
                    fontColor(
                        it.name ?: context.getString(R.string.pluto___network_state_failed),
                        context.color(R.color.pluto___red)
                    )
                )
            }
            binding.statusView.setBackgroundColor(requireContext().color(R.color.pluto___red_05))
        }

        data.response?.let {
            binding.progress.visibility = GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(
                if (it.isSuccessful) R.drawable.pluto___ic_success else R.drawable.pluto___ic_error, 0, 0, 0
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
