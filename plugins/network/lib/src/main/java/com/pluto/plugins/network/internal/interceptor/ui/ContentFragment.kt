package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.hideKeyboard
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.extensions.showKeyboard
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentContentBinding
import kotlinx.parcelize.Parcelize

class ContentFragment : Fragment(R.layout.pluto_network___fragment_content) {

    private val binding by viewBinding(PlutoNetworkFragmentContentBinding::bind)
    private val contentSharer by lazyContentSharer()
    private val argumentData: ContentFormatterData?
        get() = arguments?.getParcelable(DATA)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { handleBackPress() }
        binding.close.setOnDebounceClickListener { requireActivity().onBackPressed() }
        binding.search.setOnDebounceClickListener { binding.searchView.visibility = VISIBLE }
        binding.search.setOnDebounceClickListener {
            binding.searchView.visibility = VISIBLE
            binding.searchView.requestFocus()
        }
        binding.closeSearch.setOnDebounceClickListener { exitSearch() }
        binding.clearSearch.setOnDebounceClickListener { binding.editSearch.text = null }
        binding.editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            } else {
                v.hideKeyboard()
            }
        }
        binding.editSearch.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let { search ->
                    argumentData?.let {
                        binding.content.setSpan {
                            append(highlight(it.content, search.trim()))
                            append("\n")
                        }
                    }
                }
            }
        }
        binding.share.setOnDebounceClickListener {
            argumentData?.let {
                contentSharer.share(Shareable(title = "Share content", content = it.content.toString()))
            }
        }
        argumentData?.let {
            binding.title.text = it.title
            binding.typeFilter.text = it.typeText
            binding.contentSize.text = it.sizeText
            binding.editSearch.setText("")
        }
    }

    private fun exitSearch() {
        binding.editSearch.text = null
        binding.searchView.visibility = View.GONE
        binding.editSearch.clearFocus()
    }

    private fun handleBackPress() {
        if (binding.searchView.isVisible) {
            exitSearch()
        } else {
            findNavController().navigateUp()
        }
    }

    companion object {
        internal const val DATA = "data"
    }
}

@Parcelize
internal data class ContentFormatterData(
    val title: String,
    val content: CharSequence,
    val typeText: String? = null,
    val sizeText: String,
    val isTreeViewAllowed: Boolean = false
) : Parcelable
