package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.os.Parcelable
import android.text.Layout
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.share.Shareable
import com.pluto.plugin.share.lazyContentSharer
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentContentBinding
import com.pluto.utilities.extensions.hideKeyboard
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.showKeyboard
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding
import kotlinx.parcelize.Parcelize

internal class ContentFragment : Fragment(R.layout.pluto_network___fragment_content) {

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

                    scrollToText(search.trim())
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

    /**
     * helps to auto scroll to target search
     */
    private fun scrollToText(targetText: String) {
        if (targetText.isEmpty()) {
            return
        }

        val contentText = binding.content.getText().toString().lowercase()
        val index = contentText.indexOf(targetText.lowercase())

        if (index != -1) {
            binding.content.post {
                val layout: Layout? = binding.content.layout
                if (layout != null) {
                    val lineNumber = layout.getLineForOffset(index)
                    val x = layout.getPrimaryHorizontal(index.plus(targetText.length)).toInt()
                    val y = layout.getLineTop(lineNumber)

                    binding.horizontalScroll.smoothScrollTo(x / 2, 0)
                    binding.contentNestedScrollView.smoothScrollTo(0, y / 2)
                }
            }
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
