package com.pluto.plugin.utilities.sharing

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoLayoutContentShareOptionsBinding
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.setDebounceClickListener

class ShareOptionsDialog(
    context: Context,
    private val onAction: (ShareAction) -> Unit
) : BottomSheetDialog(context, R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto___layout_content_share_options)
    private val binding = PlutoLayoutContentShareOptionsBinding.bind(sheetView)
    private val device = Device(context)

    init {
        setContentView(sheetView)
        (sheetView.parent as View).background =
            ColorDrawable(ContextCompat.getColor(context, R.color.pluto___transparent))

        setOnShowListener { dialog ->
            if (dialog is BottomSheetDialog) {
                val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    isHideable = false
                    skipCollapsed = true
                    peekHeight = device.screen.heightPx
                }
            }
        }
    }

    fun show(shareable: Shareable) {
        binding.title.text = shareable.title
        binding.shareText.setDebounceClickListener {
            onAction.invoke(ShareAction.ShareAsText(shareable))
            dismiss()
        }
        binding.shareFile.setDebounceClickListener {
            onAction.invoke(ShareAction.ShareAsFile(shareable))
            dismiss()
        }
        binding.shareCopy.setDebounceClickListener {
            onAction.invoke(ShareAction.ShareAsCopy(shareable))
            dismiss()
        }
        show()
    }
}
