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
import com.pluto.plugins.layoutinspector.internal.inspect.getFrontView
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

class ViewHierarchyFragment : DialogFragment() {

    private var sysLayerCount: Int = 0
    private var rootView: View? = null
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
        rootView = ActivityLifecycle.topActivity?.getFrontView()

        rootView?.let {
            rootView = it.findViewById(android.R.id.content)
        }
        sysLayerCount = 0

        findViewByDefaultTag(rootView!!)?.let { view ->
            targetView = view
        } ?: run {
            targetView?.setTag(R.id.pluto_li___unique_view_tag, null)
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
        binding.expandCta.setOnDebounceClickListener {
        }
        binding.collapseCta.setOnDebounceClickListener {
        }
        hierarchyAdapter = HierarchyAdapter(onActionListener)
        binding.list.apply {
            adapter = hierarchyAdapter
        }

        viewModel.list.removeObserver(parsedAttrObserver)
        viewModel.list.observe(viewLifecycleOwner, parsedAttrObserver)
        viewModel.parse(rootView!!)
    }

    private val parsedAttrObserver = Observer<List<Hierarchy>> {
        hierarchyAdapter.list = it
    }

    private fun findViewByDefaultTag(root: View): View? {
        if (root.getTag(R.id.pluto_li___unique_view_tag) != null) {
            return root
        }
        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                val view = findViewByDefaultTag(root.getChildAt(i))
                if (view != null) {
                    return view
                }
            }
        }
        return null
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
        }
    }
}
