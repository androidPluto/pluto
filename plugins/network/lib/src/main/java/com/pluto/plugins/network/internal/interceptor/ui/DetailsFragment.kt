package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.hideKeyboard
import com.pluto.plugin.utilities.extensions.showKeyboard
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentDetailsBinding
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.DetailContentData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkViewModel
import com.pluto.plugins.network.internal.interceptor.ui.details.DetailsPagerAdapter
import com.pluto.plugins.network.internal.interceptor.ui.details.TAB_TITLES

internal class DetailsFragment : Fragment(R.layout.pluto_network___fragment_details) {

    private val binding by viewBinding(PlutoNetworkFragmentDetailsBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            }
        )
        setupPager()

        viewModel.detailContentLiveData.removeObserver(detailsObserver)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, detailsObserver)

        viewModel.apiCalls.removeObserver(listUpdateObserver)
        viewModel.apiCalls.observe(viewLifecycleOwner, listUpdateObserver)

        binding.close.setOnDebounceClickListener {
            handleBackPress()
        }
        binding.share.setOnDebounceClickListener {
            findNavController().navigate(R.id.openShareView)
        }
        binding.search.setOnDebounceClickListener {
            binding.searchView.visibility = View.VISIBLE
            binding.searchView.requestFocus()
        }
        binding.closeSearch.setOnDebounceClickListener {
            exitSearch()
        }
        binding.clearSearch.setOnDebounceClickListener {
            binding.editSearch.text = null
        }
        binding.editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            } else {
                v.hideKeyboard()
            }
        }
        binding.editSearch.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    viewModel.searchContent(it)
                }
            }
        }
    }

    private val listUpdateObserver = Observer<List<ApiCallData>> {
        val id = requireArguments().getString("id", null)
        if (!id.isNullOrEmpty()) {
            viewModel.setCurrent(id)
        } else {
            requireContext().toast("invalid id")
            requireActivity().onBackPressed()
        }
    }

    private val detailsObserver = Observer<DetailContentData> {
        binding.title.setSpan {
            append(fontColor(bold("${it.api.request.method.uppercase()}, "), requireContext().color(R.color.pluto___white_60)))
            append(it.api.request.url.encodedPath)
        }
    }

    private fun handleBackPress() {
        if (binding.searchView.isVisible) {
            exitSearch()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setupPager() {
        val pagerAdapter = DetailsPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = requireContext().getString(TAB_TITLES[position])
        }.attach()
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabs.isInlineLabel = false
    }

    private fun exitSearch() {
        binding.editSearch.text = null
        binding.searchView.visibility = View.GONE
        binding.editSearch.clearFocus()
    }
}
