package com.mocklets.pluto.modules.setup.easyaccess

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DeviceInfo
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoLayoutEasyAccessSetupBinding

internal class EasyAccessSetupDialog(context: Context, onEnabled: () -> Unit) :
    BottomSheetDialog(context, R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto___layout_easy_access_setup)
    private val binding = PlutoLayoutEasyAccessSetupBinding.bind(sheetView)
    private val deviceInfo = DeviceInfo(context)

    init {
        setContentView(sheetView)
        (sheetView.parent as View).background = ColorDrawable(context.color(R.color.pluto___transparent))

        this.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                isHideable = false
                skipCollapsed = true
                peekHeight = deviceInfo.height
            }

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        binding.cta.setDebounceClickListener {
            dismiss()
            onEnabled.invoke()
        }

        binding.dismiss.setDebounceClickListener {
            dismiss()
        }

        setOnShowListener {
            Pluto.session.isEasyAccessSetupDialogShown = true
        }
    }
}
