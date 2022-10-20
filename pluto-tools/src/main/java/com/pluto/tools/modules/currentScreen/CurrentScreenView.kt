package com.pluto.tools.modules.currentScreen

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.tools.R
import com.pluto.tools.databinding.PlutoToolCurrentScreenViewBinding
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan

internal class CurrentScreenView(context: Context) : ConstraintLayout(context) {

    val binding = PlutoToolCurrentScreenViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var lastActivityName: CharSequence? = null
    private var lastFragmentName: CharSequence? = null

    fun updateText(activity: CharSequence?, fragment: CharSequence?) {
        if ((activity ?: "").startsWith(PLUTO_PKG_PREFIX, true)) {
            updateActivity(
                context.createSpan {
                    append(light(italic(fontColor("~ Pluto Screen ~", context.color(R.color.pluto___white_60)))))
                }
            )
            updateFragment(null)
        } else {
            updateActivity(activity)
            updateFragment(fragment)
        }
    }

    private fun updateFragment(fragment: CharSequence?) {
        fragment?.let {
            binding.fragmentGroup.visibility = VISIBLE
            var value: CharSequence? = context.createSpan { append(it) }
            if (!TextUtils.isEmpty(value)) {
                lastFragmentName = binding.fragment.text
            } else {
                value = lastFragmentName
            }
            binding.fragment.text = value
        } ?: run {
            binding.fragmentGroup.visibility = GONE
        }
    }

    private fun updateActivity(activity: CharSequence?) {
        activity?.let {
            binding.activityGroup.visibility = VISIBLE
            var value: CharSequence? = context.createSpan { append(it) }
            if (!TextUtils.isEmpty(value)) {
                lastActivityName = binding.activity.text
            } else {
                value = lastActivityName
            }
            binding.activity.text = value
        } ?: run {
            binding.activityGroup.visibility = GONE
        }
    }

    companion object {
        private const val PLUTO_PKG_PREFIX = "com.pluto"
    }
}
