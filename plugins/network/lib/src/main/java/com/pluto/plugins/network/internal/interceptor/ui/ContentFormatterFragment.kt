package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentContentFormatterBinding
import kotlinx.parcelize.Parcelize

class ContentFormatterFragment : Fragment(R.layout.pluto_network___fragment_content_formatter) {

    private val binding by viewBinding(PlutoNetworkFragmentContentFormatterBinding::bind)
    private val argumentData: ContentFormatterData?
        get() = arguments?.getParcelable("data")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        binding.close.setOnDebounceClickListener { requireActivity().onBackPressed() }
        binding.horizontalScroll.layoutParams
        argumentData?.let {
            binding.title.text = it.title
            it.content?.let { content ->
                binding.content.setSpan {
                    append("\n")
                    append(content)
                    append("\n")
                }
            }
            binding.typeFilter.text = it.type
            binding.contentSize.text = it.sizeText
        }
    }
}

@Parcelize
internal data class ContentFormatterData(
    val title: String,
    val content: CharSequence?,
    val type: String,
    val sizeText: String
) : Parcelable
