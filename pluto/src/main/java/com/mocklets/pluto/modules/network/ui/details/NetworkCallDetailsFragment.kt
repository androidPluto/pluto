package com.mocklets.pluto.modules.network.ui.details

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.asFormattedDate
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.hideKeyboard
import com.mocklets.pluto.core.extensions.lazyParcelExtra
import com.mocklets.pluto.core.extensions.showKeyboard
import com.mocklets.pluto.core.sharing.Shareable
import com.mocklets.pluto.core.sharing.lazyContentSharer
import com.mocklets.pluto.core.ui.routing.OnBackKeyHandler
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoLayoutNetworkCallDetailsBinding
import com.mocklets.pluto.modules.network.ApiCallData
import com.mocklets.pluto.modules.network.flatten
import com.mocklets.pluto.modules.network.ui.DetailContentData
import com.mocklets.pluto.modules.network.ui.NetworkViewModel
import java.util.Locale
import kotlinx.android.parcel.Parcelize

internal class NetworkCallDetailsFragment : Fragment(R.layout.pluto___layout_network_call_details), OnBackKeyHandler {

    private val binding by viewBinding(PlutoLayoutNetworkCallDetailsBinding::bind)
    private val arguments by lazyParcelExtra<Data>()
    private val viewModel: NetworkViewModel by activityViewModels()
    private val contentSharer by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) {
            activity?.onBackPressed()
        }
        viewModel.apiCalls.observe(viewLifecycleOwner, listUpdateObserver)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, apiCallObserver)
        setupPager()
        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }
        binding.share.setDebounceClickListener {
            viewModel.detailContentLiveData.value?.let {
                contentSharer.share(
                    Shareable(
                        title = "Share Network Call details",
                        content = it.api.toShareText(),
                        fileName = "Network Call details from Pluto"
                    )
                )
            }
        }
        binding.search.setDebounceClickListener {
            binding.searchView.visibility = VISIBLE
            binding.searchView.requestFocus()
        }
        binding.closeSearch.setDebounceClickListener {
            exitSearch()
        }
        binding.clearSearch.setDebounceClickListener {
            binding.editSearch.text = null
        }
        binding.editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            } else {
                v.hideKeyboard()
            }
        }
        binding.editSearch.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
                    viewModel.searchContent(it)
                }
            }
        }
    }

    private fun exitSearch() {
        binding.editSearch.text = null
        binding.searchView.visibility = GONE
        binding.editSearch.clearFocus()
    }

    private fun setupPager() {
        val pagerAdapter = NetworkDetailsPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = context?.getString(TAB_TITLES[position])
        }.attach()
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabs.isInlineLabel = false
    }

    private val listUpdateObserver = Observer<List<ApiCallData>> {
        arguments?.id?.let { id ->
            viewModel.fetchCurrent(id)
        }
    }

    private val apiCallObserver = Observer<DetailContentData> {
        val endPoint = StringBuilder()
        it.api.request.url.pathSegments().forEach { segment ->
            endPoint.append("/$segment")
        }
        binding.title.setSpan {
            append(
                fontColor(
                    it.api.request.method.uppercase(Locale.getDefault()),
                    context.color(R.color.pluto___text_dark_60)
                )
            )
            append("  $endPoint")
        }
    }

    @Parcelize
    data class Data(val id: String) : Parcelable

    override fun onBackPressed(): Boolean {
        if (binding.searchView.isVisible) {
            exitSearch()
            return true
        }
        return false
    }
}

private const val STACK_TRACE_LENGTH = 10
@Suppress("StringLiteralDuplication")
private fun ApiCallData.toShareText(): String {
    val text = StringBuilder()
    text.append("${request.method.uppercase(Locale.getDefault())}, ${request.url}  ")
    if (response != null) {
        text.append(
            "\n\nRequested at : ${response!!.sendTimeMillis.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
        text.append(
            "\nReceived at : ${response!!.receiveTimeMillis.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
        text.append("\nDelay : ${response!!.receiveTimeMillis - response!!.sendTimeMillis} ms")
        text.append("\nProtocol : ${response!!.protocol.name}")
    } else {
        text.append(
            "\n\nRequested at : ${request.timestamp.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
    }
    text.append("\n\n==================\n\n")
    text.append("REQUEST")
    text.append("\n\nHeaders : \n${Gson().toJson(request.headers)}")
    text.append("\n\nBody :\n${request.body.flatten()}")

    response?.let { response ->
        text.append("\n\n==================\n\n")
        text.append("RESPONSE")
        text.append("\n\nHeaders : \n${Gson().toJson(response.headers)}")
        text.append("\n\nBody : \n${response.body.flatten()}")
    }
    exception?.let { exception ->
        text.append("\n\n==================\n\n")
        text.append("RESPONSE\n")
        text.append("\n${exception.name}: ${exception.message}\n")
        exception.stackTrace.take(STACK_TRACE_LENGTH).forEach {
            text.append("\t at $it\n")
        }
        if (exception.stackTrace.size - STACK_TRACE_LENGTH > 0) {
            text.append("\t + ${exception.stackTrace.size - STACK_TRACE_LENGTH} more lines")
        }
    }
    text.append("\n\n==================\n")
    text.append("report generated by Pluto (https://pluto.mocklets.com)")
    return text.toString()
}
