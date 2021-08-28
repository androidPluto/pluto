package com.mocklets.pluto.core.sharing

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DeviceInfo
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoLayoutContentShareOptionsBinding

internal class ShareOptionsDialog(context: Context) : BottomSheetDialog(context, R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto___layout_content_share_options)
    private val binding = PlutoLayoutContentShareOptionsBinding.bind(sheetView)
    private val deviceInfo = DeviceInfo(context)

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
                    peekHeight = deviceInfo.height
                }
            }
        }
    }

    fun show(context: Context, shareable: Shareable) {
        binding.title.text = shareable.title
        binding.shareText.setDebounceClickListener {
            context.share(shareable.content, shareable.title)
            dismiss()
        }
        binding.shareFile.setDebounceClickListener {
            context.shareFile(shareable.content, shareable.title, shareable.fileName)
            dismiss()
        }
        binding.shareCopy.setDebounceClickListener {
            context.copyToClipboard(shareable.content, shareable.title)
            context.toast("Content copied to Clipboard")
            dismiss()
        }
        show()
    }
}
