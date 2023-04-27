package com.pluto.plugins.layoutinspector.internal.attributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiFragmentViewAttrBinding
import com.pluto.plugins.layoutinspector.internal.ActivityLifecycle
import com.pluto.plugins.layoutinspector.internal.attributes.data.MutableAttribute
import com.pluto.plugins.layoutinspector.internal.attributes.list.AttributeAdapter
import com.pluto.plugins.layoutinspector.internal.inspect.clearTargetTag
import com.pluto.plugins.layoutinspector.internal.inspect.findViewByTargetTag
import com.pluto.plugins.layoutinspector.internal.inspect.getFrontView
import com.pluto.plugins.layoutinspector.internal.inspect.getIdString
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.color
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.share.ContentShareViewModel
import com.pluto.utilities.share.Shareable
import com.pluto.utilities.share.lazyContentSharer
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding
import com.pluto.utilities.views.keyvalue.KeyValuePairEditResult
import com.pluto.utilities.views.keyvalue.edit.KeyValuePairEditor
import com.pluto.utilities.views.keyvalue.edit.lazyKeyValuePairEditor

internal class ViewAttrFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoLiFragmentViewAttrBinding::bind)
    private val contentSharer: ContentShareViewModel by lazyContentSharer()
    private var targetView: View? = null
    private val attributeAdapter: BaseAdapter by autoClearInitializer {
        AttributeAdapter(onActionListener)
    }
    private val viewModel: ViewAttrViewModel by viewModels()
    private val keyValuePairEditor: KeyValuePairEditor by lazyKeyValuePairEditor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_li___fragment_view_attr, container, false)

    override fun getTheme(): Int = R.style.PlutoLIBottomSheetDialog

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLifecycle.topActivity?.let {
            it.getFrontView().findViewByTargetTag()?.let { view ->
                targetView = view
            } ?: run {
                targetView?.clearTargetTag()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = Device(requireContext()).screen.heightPx
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        targetView?.let { target ->
            binding.close.setOnDebounceClickListener {
                dismiss()
            }
            binding.share.setOnDebounceClickListener {
                viewModel.shareableAttr.value?.let {
                    contentSharer.share(
                        Shareable(
                            title = "Sharing View Attributes", content = it, fileName = "View Attributes generated via Pluto"
                        )
                    )
                }
            }
            binding.title.setSpan {
                append(semiBold(target.javaClass.simpleName))
                append("\n")
                target.getIdString()?.let {
                    append(regular(fontSize(it, SUBTITLE_TEXT_SIZE_IN_SP)))
                } ?: run {
                    append(regular(fontSize(italic(fontColor("NO_ID", context.color(R.color.pluto___text_dark_40))), SUBTITLE_TEXT_SIZE_IN_SP)))
                }
            }
            binding.attrList.apply {
                adapter = attributeAdapter
            }

            keyValuePairEditor.result.removeObserver(keyValuePairEditObserver)
            keyValuePairEditor.result.observe(viewLifecycleOwner, keyValuePairEditObserver)

            viewModel.list.removeObserver(parsedAttrObserver)
            viewModel.list.observe(viewLifecycleOwner, parsedAttrObserver)
            viewModel.parse(target)
        }
    }

    private val parsedAttrObserver = Observer<List<ListItem>> {
        binding.share.visibility = VISIBLE
        attributeAdapter.list = it
    }

    private val keyValuePairEditObserver = Observer<KeyValuePairEditResult> {
        targetView?.let { view ->
            it.value?.let { value ->
                if (it.metaData is MutableAttribute) {
                    viewModel.updateAttributeValue(view, it.metaData as MutableAttribute, value)
                }
            }
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is MutableAttribute) {
                keyValuePairEditor.edit(data.requestEdit())
            }
        }
    }

    private companion object {
        const val SUBTITLE_TEXT_SIZE_IN_SP = 12
    }
}
