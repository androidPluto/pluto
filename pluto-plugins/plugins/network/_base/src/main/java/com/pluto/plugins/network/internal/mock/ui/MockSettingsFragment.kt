package com.pluto.plugins.network.internal.mock.ui

import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugins.network.base.R
import com.pluto.plugins.network.base.databinding
    .PlutoNetworkFragmentMockSettingsBinding
import com.pluto.plugins.network.internal.customTab
import com.pluto.plugins.network.internal.interceptor.logic.pruneQueryParams
import com.pluto.plugins.network.internal.mock.logic.dao.MockData
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.delayedLaunchWhenResumed
import com.pluto.utilities.extensions.fadeInAndOut
import com.pluto.utilities.extensions.hideKeyboard
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class MockSettingsFragment : Fragment(R.layout.pluto_network___fragment_mock_settings) {

    private val binding by viewBinding(PlutoNetworkFragmentMockSettingsBinding::bind)
    private val viewModel: MockSettingsViewModel by activityViewModels()
    private var requestConfig: RequestConfig? = null
    private var isCustomTabOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestConfig = convertArguments(arguments)
        onBackPressed { findNavController().navigateUp() }

        viewModel.currentMock.removeObservers(viewLifecycleOwner)
        viewModel.currentMock.observe(viewLifecycleOwner, mockObserver)

        viewModel.mockletsUrlEvent.removeObservers(viewLifecycleOwner)
        viewModel.mockletsUrlEvent.observe(viewLifecycleOwner, mockletsUrlEventObserver)

        viewModel.event.removeObservers(viewLifecycleOwner)
        viewModel.event.observe(viewLifecycleOwner, eventsObserver)

        requestConfig?.let { viewModel.fetch(it.url, it.method) }

        binding.back.setOnDebounceClickListener {
            requireActivity().hideKeyboard()
            findNavController().navigateUp()
        }
        binding.save.setOnDebounceClickListener {
            requestConfig?.let {
                viewModel.update(
                    binding.endPoint.text.toString(),
                    it.method,
                    MockData(url = binding.proxyUrl.text.toString())
                )
            }
            requireActivity().hideKeyboard()
        }
        binding.delete.setOnDebounceClickListener {
            requireActivity().hideKeyboard()
            requestConfig?.url?.let { viewModel.delete(it) }
        }
        binding.method.text = requestConfig?.method?.uppercase()
        binding.accessMocklets.setOnDebounceClickListener {
            val uri = Uri.parse(MOCKLETS_URL)
                .buildUpon()
                .appendQueryParameter(METHOD_PARAM, requestConfig?.method)
                .build()
            requireActivity().customTab(uri)
            isCustomTabOpen = true
        }
    }

    private val eventsObserver = Observer<Pair<Boolean, String>> {
        requireContext().toast(it.second)
        if (it.first) {
            requireActivity().onBackPressed()
        }
    }

    private fun setupUpdateUI(entity: MockSettingsEntity) {
        binding.endPoint.isEnabled = false
        binding.delete.visibility = View.VISIBLE
        binding.note.visibility = View.VISIBLE
        context?.let { binding.endPoint.setTextColor(it.color(R.color.pluto___text_dark_40)) }
        binding.endPoint.setText(entity.requestUrl)
        binding.proxyUrl.setText(entity.mockData.url)
    }

    private val mockObserver = Observer<MockSettingsEntity?> {
        binding.endPoint.setText(requestConfig?.url?.pruneQueryParams())
        binding.delete.visibility = View.GONE
//        binding.divider.visibility = View.GONE
        it?.let {
            setupUpdateUI(it)
        }
        binding.proxyUrl.setSelection(binding.proxyUrl.text.toString().length)
    }

    private val mockletsUrlEventObserver = Observer<String> {
        binding.proxyUrl.requestFocus()
        viewLifecycleOwner.lifecycleScope.delayedLaunchWhenResumed(CLIPBOARD_PROCESS_DELAY) {
            context?.clipboardData()?.let { text ->
                if (text != binding.proxyUrl.text.toString() && isSelectionValid(text)) {
                    binding.proxyUrl.setText(text)
                    binding.mockletsSuccess.fadeInAndOut(viewLifecycleOwner.lifecycleScope)
                }
            }
            binding.proxyUrl.clearFocus()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isCustomTabOpen) {
            isCustomTabOpen = false
            viewModel.onInAppBrowserClose()
        }
    }

    private fun Context.clipboardData(): String? {
        val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
    }

    private fun isSelectionValid(it: String): Boolean =
        it.startsWith(MOCKLETS_API_PREFIX) && binding.proxyUrl.text.toString() != it

    companion object {
        const val IN_APP_BROWSER_RESULT_CODE = 10_001
        const val MOCKLETS_URL = "https://connect.mocklets.com?ref=pluto"
        const val MOCKLETS_API_PREFIX = "https://api.mocklets.com/"
        const val ARG_URL = "url"
        const val ARG_METHOD = "method"
        const val METHOD_PARAM = "method"
        const val CLIPBOARD_PROCESS_DELAY = 50L
    }

    private fun convertArguments(arguments: Bundle?): RequestConfig? {
        arguments?.let {
            if (it.getString(ARG_URL) != null && it.getString(ARG_METHOD) != null) {
                return RequestConfig(it.getString(ARG_URL), (it.getString(ARG_METHOD) ?: "any").lowercase())
            }
            return null
        }
        return null
    }
}

@Keep
internal data class RequestConfig(
    val url: String?,
    val method: String
)
