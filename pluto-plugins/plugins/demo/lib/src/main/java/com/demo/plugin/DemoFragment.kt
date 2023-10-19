package com.demo.plugin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.demo.plugin.databinding.DemoFragmentDemoBinding
import com.pluto.plugin.share.Shareable
import com.pluto.plugin.share.lazyContentSharer
import com.pluto.utilities.extensions.showMoreOptions
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

class DemoFragment : Fragment(R.layout.demo___fragment_demo) {

    private val binding by viewBinding(DemoFragmentDemoBinding::bind)
    private val contentSharer by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnDebounceClickListener {
            activity?.finish()
        }
        binding.options.setOnDebounceClickListener {
            context?.showMoreOptions(it, R.menu.demo___menu_more_options) { item ->
                when (item.itemId) {
                    R.id.action1 -> requireContext().toast("Demo Action 1 clicked")
                    R.id.action2 -> requireContext().toast("Demo Action 2 clicked")
                }
            }
        }
        binding.share.setOnDebounceClickListener {
            contentSharer.share(
                Shareable(
                    title = "Demo share",
                    content = "demo content",
                    fileName = "demo file"
                )
            )
        }
        binding.notification.setOnDebounceClickListener {
            DemoNotification(requireContext()).add()
        }
    }
}
