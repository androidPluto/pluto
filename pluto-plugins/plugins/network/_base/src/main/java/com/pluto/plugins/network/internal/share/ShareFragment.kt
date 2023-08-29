package com.pluto.plugins.network.internal.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.network.base.R
import com.pluto.plugins.network.base.databinding
    .PlutoNetworkFragmentShareBinding
import com.pluto.plugins.network.internal.interceptor.logic.responseToText
import com.pluto.plugins.network.internal.interceptor.logic.toShareText
import com.pluto.plugins.network.internal.interceptor.ui.DetailContentData
import com.pluto.plugins.network.internal.interceptor.ui.NetworkViewModel
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.share.Shareable
import com.pluto.utilities.share.lazyContentSharer
import com.pluto.utilities.viewBinding

class ShareFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoNetworkFragmentShareBinding::bind)
    private val detailsViewModel: NetworkViewModel by activityViewModels()
    private val shareViewModel: ShareOptionsViewModel by viewModels()
    private val optionAdapter: BaseAdapter by autoClearInitializer { ShareOptionsAdapter(onActionListener) }
    private val contentSharer by lazyContentSharer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_network___fragment_share, container, false)

    override fun getTheme(): Int = R.style.PlutoNetworkBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shareOptionsList.apply {
            adapter = optionAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }

        detailsViewModel.detailContentLiveData.removeObserver(detailsObserver)
        detailsViewModel.detailContentLiveData.observe(viewLifecycleOwner, detailsObserver)

        shareViewModel.shareOptions.removeObserver(shareOptionsObserver)
        shareViewModel.shareOptions.observe(viewLifecycleOwner, shareOptionsObserver)
    }

    private val detailsObserver = Observer<DetailContentData> {
        shareViewModel.generate(it.api)
    }

    private val shareOptionsObserver = Observer<List<ShareOptionType>> {
        optionAdapter.list = it
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is ShareOptionType) {
                detailsViewModel.detailContentLiveData.value?.let {
                    val shareContent: String? = when (data) {
                        is ShareOptionType.All -> it.api.toShareText()
                        is ShareOptionType.CURL -> it.api.curl
                        is ShareOptionType.Header -> null
                        is ShareOptionType.Request -> it.api.request.toShareText()
                        is ShareOptionType.Response -> it.api.responseToText()
                    }
                    shareContent?.let { content ->
                        contentSharer.share(
                            Shareable(
                                title = "Share Network Call details",
                                content = content,
                                fileName = "Network Call details from Pluto"
                            )
                        )
                    }
                }
            }
        }
    }
}
