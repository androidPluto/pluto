package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewHierarchyBinding
import com.pluto.plugins.layoutinspector.internal.ActivityLifecycle
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyAdapter
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyItemHolder.Companion.ACTION_ATTRIBUTE
import com.pluto.plugins.layoutinspector.internal.hierarchy.list.HierarchyItemHolder.Companion.ACTION_EXPAND_COLLAPSE
import com.pluto.plugins.layoutinspector.internal.inspect.clearTargetTag
import com.pluto.plugins.layoutinspector.internal.inspect.findViewByTargetTag
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

    private var sysLayerCount: Int = 0
    private var targetView: View? = null
    private lateinit var hierarchyAdapter: BaseAdapter
    private val viewModel: ViewHierarchyViewModel by viewModels()
    private val binding by viewBinding(PlutoLiFragmentViewHierarchyBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pluto_li___fragment_view_hierarchy, container, false)
    }

    override fun getTheme(): Int = R.style.PlutoLIFullScreenDialogStyle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            return
        }


//        targetView = findViewByDefaultTag(rootView!!)
//        if (targetView != null) {
//            // clear flag
//            targetView.setTag(R.id.pd_view_tag_for_unique, null)
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        binding.close.setOnDebounceClickListener {
            findNavController().navigateUp()
        }

        ActivityLifecycle.topActivity?.getFrontView()?.let {
            val rootView: View = it.findViewById(android.R.id.content)
            sysLayerCount = 0

            rootView.findViewByTargetTag()?.let { view ->
                targetView = view
            } ?: run {
                targetView?.clearTargetTag()
            }

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

    private val parsedAttrObserver = Observer<List<Hierarchy>> {
        val list = arrayListOf<Hierarchy>()
        list.addAll(it)
        hierarchyAdapter.list = list
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is Hierarchy) {
                when (action) {
                    ACTION_ATTRIBUTE -> toast("attribute")
                    ACTION_EXPAND_COLLAPSE -> {
                        if(data.isExpanded) {
                            viewModel.removeChildren(data, holder.layoutPosition)
                        } else {
                            viewModel.addChildren(data, holder.layoutPosition)
                        }
                    }
                }
            }
        }
    }
}
