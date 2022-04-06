package com.pluto.plugins

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.R
import com.pluto.databinding.PlutoFragmentDevDetailsBinding
import com.pluto.plugins.utilities.setDebounceClickListener
import com.pluto.plugins.utilities.spannable.setSpan
import com.pluto.plugins.utilities.viewBinding

internal class DevDetailsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentDevDetailsBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_dev_details, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("icon")?.let { binding.icon.setImageResource(it) }
        arguments?.getString("name")?.let { binding.name.text = it }
        arguments?.getString("version")?.let {
            binding.version.setSpan {
                append(light(fontColor("ver ", R.color.pluto___text_dark_60)))
                append(bold(fontColor(it, R.color.pluto___text_dark)))
            }
        }
        arguments?.getString("website")?.let {
            binding.website.text = it
            binding.website.setDebounceClickListener { _ ->
                openUrl(it)
            }
        }
        arguments?.getString("vcs")?.let {
            binding.vcsLink.text = it
            binding.vcsLink.setDebounceClickListener { _ ->
                openUrl(it)
            }
        }
        arguments?.getString("twitter")?.let {
            binding.twitterLink.text = it
            binding.twitterLink.setDebounceClickListener { _ ->
                openUrl(it)
            }
        }
    }

    private fun openUrl(it: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        requireContext().startActivity(browserIntent)
    }
}
