package com.mocklets.pluto.network.ui.proxy

import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkFragmentNetworkProxySettingsBinding
import com.mocklets.pluto.network.internal.proxy.NetworkProxyViewModel
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.network.internal.proxy.dao.ProxyData
import com.mocklets.pluto.network.internal.pruneQueryParams
import com.mocklets.pluto.utilities.extensions.color
import com.mocklets.pluto.utilities.extensions.delayedLaunchWhenResumed
import com.mocklets.pluto.utilities.extensions.fadeInAndOut
import com.mocklets.pluto.utilities.extensions.hideKeyboard
import com.mocklets.pluto.utilities.extensions.lazyParcelExtra
import com.mocklets.pluto.utilities.extensions.toast
import com.mocklets.pluto.utilities.routing.lazyRouter
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.spannable.setSpan
import com.mocklets.pluto.utilities.viewBinding
import kotlinx.parcelize.Parcelize

internal class NetworkProxySettingsFragment : Fragment(R.layout.pluto_network___fragment_network_proxy_settings) {

    private val binding by viewBinding(PlutoNetworkFragmentNetworkProxySettingsBinding::bind)
    private val arguments by lazyParcelExtra<Data>()
    private val viewModel: NetworkProxyViewModel by activityViewModels()
    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentProxy.removeObservers(viewLifecycleOwner)
        viewModel.currentProxy.observe(viewLifecycleOwner, apiCallObserver)
        arguments?.let { viewModel.fetch(it.url, it.method) }
        arguments?.showListCta?.let {
            if (!it) {
                binding.seeAll.visibility = GONE
                binding.divider.visibility = GONE
            }
        }
        binding.requestLabel.setSpan {
            append(getString(R.string.pluto_network___network_proxy_settings_request_label))
            arguments?.let {
                append(bold(fontColor(" (", context.color(R.color.pluto___text_dark_60))))
                append(bold(fontColor(it.method, context.color(R.color.pluto___text_dark))))
                append(bold(fontColor(")", context.color(R.color.pluto___text_dark_60))))
            }
        }

        binding.seeAll.setDebounceClickListener {
            requireActivity().hideKeyboard()
            activity?.onBackPressed()
            router.navigate(Screens.NetworkProxySettingsList)
        }
        binding.close.setDebounceClickListener {
            requireActivity().hideKeyboard()
            activity?.onBackPressed()
        }
        binding.save.setDebounceClickListener {
            arguments?.let {
                viewModel.update(
                    binding.endPoint.text.toString(),
                    it.method,
                    ProxyData(url = binding.proxyUrl.text.toString())
                )
            }
            requireActivity().hideKeyboard()
        }
        binding.delete.setDebounceClickListener {
            requireActivity().hideKeyboard()
            viewModel.delete(binding.endPoint.text.toString())
        }
        binding.accessMocklets.setDebounceClickListener {
            val uri = Uri.parse(MOCKLETS_URL)
                .buildUpon()
                .appendQueryParameter(METHOD_PARAM, arguments?.method?.lowercase() ?: "any")
                .build()
            requireActivity().customTab(uri)
        }

        viewModel.event.removeObservers(viewLifecycleOwner)
        viewModel.event.observe(viewLifecycleOwner, eventsObserver)

        viewModel.mockletsUrl.removeObservers(viewLifecycleOwner)
        viewModel.mockletsUrl.observe(viewLifecycleOwner, mockletsUrlObserver)
    }

    private val eventsObserver = Observer<Pair<Boolean, String>> {
        context?.toast(it.second)
        if (it.first) {
            activity?.onBackPressed()
        }
    }

    private val mockletsUrlObserver = Observer<String> {
        binding.proxyUrl.requestFocus()
        lifecycleScope.delayedLaunchWhenResumed(CLIPBOARD_PROCESS_DELAY) {
            context?.clipboardData()?.let {
                if (isSelectionValid(it)) {
                    binding.proxyUrl.setText(it)
                    binding.mockletsSuccess.fadeInAndOut(lifecycleScope)
                }
            }
            binding.proxyUrl.clearFocus()
        }
    }

    private fun isSelectionValid(it: String): Boolean =
        it.startsWith(MOCKLETS_API_PREFIX) && binding.proxyUrl.text.toString() != it

    private val apiCallObserver = Observer<NetworkProxyEntity?> {
        binding.endPoint.setText(arguments?.url?.pruneQueryParams())
        binding.delete.visibility = GONE
        binding.divider.visibility = GONE
        it?.let {
            setupUpdateUI(it)
        }
        binding.proxyUrl.setSelection(binding.proxyUrl.text.toString().length)
    }

    private fun setupUpdateUI(entity: NetworkProxyEntity) {
        binding.endPoint.isEnabled = false
        binding.delete.visibility = VISIBLE
        binding.note.visibility = VISIBLE
        arguments?.showListCta?.let {
            if (it) {
                binding.divider.visibility = VISIBLE
            }
        }
        context?.let { binding.endPoint.setTextColor(it.color(R.color.pluto___text_dark_40)) }
        binding.endPoint.setText(entity.requestUrl)
        binding.proxyUrl.setText(entity.proxyData.url)
    }

    private fun Context.clipboardData(): String? {
        val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
    }

    @Parcelize
    data class Data(val url: String, val method: String, val showListCta: Boolean = false) : Parcelable

    companion object {
        const val IN_APP_BROWSER_RESULT_CODE = 10_001
        const val MOCKLETS_URL = "https://connect.mocklets.com?ref=pluto"
        const val MOCKLETS_API_PREFIX = "https://api.mocklets.com/"
        const val METHOD_PARAM = "method"
        const val CLIPBOARD_PROCESS_DELAY = 50L
    }
}
