package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewHierarchyBinding
import com.pluto.plugins.layoutinspector.internal.ActivityLifecycle
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyAdapter
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyItemHolder.Companion.ACTION_ATTRIBUTE
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyItemHolder.Companion.ACTION_EXPAND_COLLAPSE
import com.pluto.plugins.layoutinspector.internal.inspect.InspectViewModel
import com.pluto.plugins.layoutinspector.internal.inspect.getFrontView
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class ViewHierarchyFragment : DialogFragment() {

    private lateinit var hierarchyAdapter: BaseAdapter
    private val viewModel: ViewHierarchyViewModel by viewModels()
    private val inspectViewModel: InspectViewModel by activityViewModels()
    private val binding by viewBinding(PlutoLiFragmentViewHierarchyBinding::bind)
    private var hasAlreadyScrolled: Boolean = false
    private val shouldScrollToInspectedView: Boolean
        get() = arguments?.getBoolean(SCROLL_TO_TARGET) ?: false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pluto_li___fragment_view_hierarchy, container, false)
    }

    override fun getTheme(): Int = R.style.PlutoLIFullScreenDialogStyle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        binding.close.setOnDebounceClickListener {
            findNavController().navigateUp()
        }

        ActivityLifecycle.topActivity?.getFrontView()?.let {
            val rootView: View = it.findViewById(android.R.id.content)

            binding.expandCta.setOnDebounceClickListener {
                viewModel.expandAll(rootView)
            }
            binding.collapseCta.setOnDebounceClickListener {
                viewModel.collapseAll(rootView)
            }
            hierarchyAdapter = HierarchyAdapter(onActionListener)
            binding.list.apply {
                adapter = hierarchyAdapter
            }

            viewModel.list.removeObserver(parsedAttrObserver)
            viewModel.list.observe(viewLifecycleOwner, parsedAttrObserver)
            viewModel.parseInit(rootView)
        } ?: run {
            toast("root view not found, go back & try again")
        }
    }

    private fun scrollToInspectedView(list: List<Hierarchy>) {
        var inspectedViewIndex = list.indexOfFirst { it.isTargetView }
        if (inspectedViewIndex > -1) {
            if (inspectedViewIndex < list.size - 1) inspectedViewIndex++
            binding.list.smoothScrollToPosition(inspectedViewIndex)
        }
    }

    private val parsedAttrObserver = Observer<List<Hierarchy>> {
        hierarchyAdapter.list = arrayListOf<Hierarchy>().apply {
            addAll(it)
        }

        if (shouldScrollToInspectedView && !hasAlreadyScrolled) {
            scrollToInspectedView(it)
            hasAlreadyScrolled = true
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is Hierarchy) {
                when (action) {
                    ACTION_ATTRIBUTE -> {
                        inspectViewModel.select(data.view)
                        dismiss()
                    }

                    ACTION_EXPAND_COLLAPSE ->
                        if (data.isExpanded) {
                            viewModel.removeChildren(data, holder?.layoutPosition ?: 0)
                        } else {
                            viewModel.addChildren(data, holder?.layoutPosition ?: 0)
                        }
                }
            }
        }
    }

    companion object {
        const val SCROLL_TO_TARGET = "scroll_to_target"
    }
}
