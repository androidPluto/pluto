package com.pluto.plugin.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoFragmentShareBinding
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

class ShareFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentShareBinding::bind)
    private val shareViewModel: ContentShareViewModel by activityViewModels()
    private var shareContent: Shareable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_share, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareContent = arguments?.toShareable()

        binding.title.text = shareContent?.title ?: getString(R.string.pluto___share_as)

        binding.shareCopy.setOnDebounceClickListener {
            shareContent?.let {
                shareViewModel.performAction(ShareAction.ShareAsCopy(it))
            }
            dismiss()
        }

        binding.shareText.setOnDebounceClickListener {
            shareContent?.let {
                shareViewModel.performAction(ShareAction.ShareAsText(it))
            }
            dismiss()
        }

        binding.shareFile.setOnDebounceClickListener {
            shareContent?.let {
                shareViewModel.performAction(ShareAction.ShareAsFile(it))
            }
            dismiss()
        }
    }
}

private fun Bundle?.toShareable(): Shareable? {
    this?.let {
        return if (it.getString("title") != null &&
            it.getString("content") != null &&
            it.getString("fileName") != null
        ) {
            Shareable(
                title = it.getString("title") ?: "",
                content = it.getString("content") ?: "",
                fileName = it.getString("fileName") ?: "",
            )
        } else {
            null
        }
    }
    return null
}
