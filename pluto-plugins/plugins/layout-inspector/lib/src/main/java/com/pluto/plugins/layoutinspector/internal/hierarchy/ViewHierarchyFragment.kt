package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyItemHolder.Companion.ACTION_INSPECT_VIEW
import com.pluto.plugins.layoutinspector.internal.inspect.InspectViewModel
import com.pluto.plugins.layoutinspector.internal.inspect.assignTargetTag
import com.pluto.plugins.layoutinspector.internal.inspect.getFrontView
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class ViewHierarchyFragment : DialogFragment() {

    private var rootView: View? = null
    private val hierarchyAdapter: BaseAdapter by autoClearInitializer {
        HierarchyAdapter(onActionListener)
    }
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
            rootView = it.findViewById(android.R.id.content)
        } ?: run {
            toast("root view not found, go back & try again")
        }

        rootView?.let { root ->
            binding.expandCta.setOnDebounceClickListener {
                viewModel.expandAll(root)
            }
            binding.collapseCta.setOnDebounceClickListener {
                viewModel.collapseAll(root)
            }
            binding.list.apply {
                adapter = hierarchyAdapter
            }

            viewModel.list.removeObserver(parsedAttrObserver)
            viewModel.list.observe(viewLifecycleOwner, parsedAttrObserver)
            viewModel.parseInit(root)
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
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is Hierarchy) {
                when (action) {
                    ACTION_INSPECT_VIEW -> inspectView(data.view)
                    ACTION_ATTRIBUTE -> showViewAttribute(data.view)
                    ACTION_EXPAND_COLLAPSE -> collapseExpandView(data, holder)
                }
            }
        }
    }

    private fun inspectView(view: View) {
        if (view.isVisible) {
            inspectViewModel.select(view)
            dismiss()
        } else {
            context?.toast("View is not visible")
        }
    }

    private fun showViewAttribute(view: View) {
        if (view.isVisible) {
            inspectViewModel.select(view)
            rootView?.let { viewModel.parseInit(it) }
            findNavController().navigate(R.id.openAttrView)
        } else {
            view.assignTargetTag()
            findNavController().navigate(R.id.openAttrView)
        }
    }

    private fun collapseExpandView(data: Hierarchy, holder: DiffAwareHolder) {
        if (data.isExpanded) {
            viewModel.removeChildren(data, holder.layoutPosition)
        } else {
            viewModel.addChildren(data, holder.layoutPosition)
        }
    }

    companion object {
        const val SCROLL_TO_TARGET = "scroll_to_target"
    }
}
