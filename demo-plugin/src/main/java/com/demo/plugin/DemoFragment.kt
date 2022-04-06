package com.demo.plugin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.demo.plugin.databinding.DemoFragmentDemoBinding
import com.pluto.plugins.utilities.extensions.showMoreOptions
import com.pluto.plugins.utilities.extensions.toast
import com.pluto.plugins.utilities.setDebounceClickListener
import com.pluto.plugins.utilities.sharing.Shareable
import com.pluto.plugins.utilities.sharing.lazyContentSharer
import com.pluto.plugins.utilities.viewBinding

class DemoFragment : Fragment(R.layout.demo___fragment_demo) {

    private val binding by viewBinding(DemoFragmentDemoBinding::bind)
    private val contentSharer by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setDebounceClickListener {
            activity?.finish()
        }
        binding.options.setDebounceClickListener {
            context?.showMoreOptions(it, R.menu.demo___menu_more_options) { item ->
                when (item.itemId) {
                    R.id.action1 -> requireContext().toast("Demo Action 1 clicked")
                    R.id.action2 -> requireContext().toast("Demo Action 2 clicked")
                }
            }
        }
        binding.share.setDebounceClickListener {
            contentSharer.share(
                Shareable(
                    title = "Demo share",
                    content = "demo content",
                    fileName = "demo file"
                )
            )
        }
        binding.notification.setDebounceClickListener {
            DemoNotification(requireContext()).add()
        }
    }
}
