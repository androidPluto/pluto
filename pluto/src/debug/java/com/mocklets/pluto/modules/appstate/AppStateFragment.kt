package com.mocklets.pluto.modules.appstate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.Pluto.appProperties
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.dp
import com.mocklets.pluto.core.extensions.hideKeyboard
import com.mocklets.pluto.core.extensions.showKeyboard
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.sharing.Shareable
import com.mocklets.pluto.core.sharing.copyToClipboard
import com.mocklets.pluto.core.sharing.lazyContentSharer
import com.mocklets.pluto.core.ui.list.*
import com.mocklets.pluto.core.ui.routing.OnBackKeyHandler
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoFragmentAppStateBinding

internal class AppStateFragment : Fragment(R.layout.pluto___fragment_app_state), OnBackKeyHandler {

    private val binding by viewBinding(PlutoFragmentAppStateBinding::bind)
    private val appStateAdapter: BaseAdapter by lazy { AppStateItemAdapter(onActionListener) }
    private val viewModel: AppStateViewModel by activityViewModels()
    private val contentSharer by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = appStateAdapter
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }
        if (appProperties.isNotEmpty()) {
            binding.noAppPropertiesGroup.visibility = GONE
            binding.note.visibility = VISIBLE
        } else {
            binding.noAppPropertiesGroup.visibility = VISIBLE
            binding.note.visibility = GONE
        }

        binding.share.setDebounceClickListener {
            viewModel.properties.value?.let {
                contentSharer.share(
                    Shareable(
                        title = "Share App Properties",
                        content = it.toShareText(),
                        fileName = "App Properties from Pluto"
                    )
                )
            }
        }

        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }
        binding.noAppPropertiesCta.setDebounceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pluto.mocklets.com/#doc_set_properties"))
            startActivity(browserIntent)
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
                    viewModel.filter(it)
                }
            }
        }

        viewModel.properties.removeObserver(appPropertiesObserver)
        viewModel.properties.observe(viewLifecycleOwner, appPropertiesObserver)
        viewModel.filter()
    }

    private val appPropertiesObserver = Observer<List<AppStateItem>> {
        if (it.isNotEmpty()) {
            binding.share.visibility = VISIBLE
            binding.divider.visibility = VISIBLE
        }
        appStateAdapter.list = it
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            when (data) {
                is AppStateItem -> {
                    context?.copyToClipboard("${data.key} : ${data.value}", "app_state")
                    context?.toast("${data.key} copied!")
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        if (binding.searchView.isVisible) {
            exitSearch()
            return true
        }
        return false
    }

    private fun exitSearch() {
        binding.editSearch.text = null
        binding.searchView.visibility = GONE
        binding.editSearch.clearFocus()
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}

private fun List<AppStateItem>.toShareText(): String {
    val text = StringBuilder()
    text.append("App Properties : \n\n")
    this.forEach {
        text.append("${it.key} : ${it.value}\n")
    }
    return text.toString()
}

data class AppStateItem(
    val key: String,
    val value: String?
) : ListItem()
