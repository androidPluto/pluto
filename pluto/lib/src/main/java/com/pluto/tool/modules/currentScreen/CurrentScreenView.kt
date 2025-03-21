package com.pluto.tool.modules.currentScreen

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.R
import com.pluto.databinding.PlutoToolCurrentScreenViewBinding
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan

/**
 * View that displays the current activity and fragment names.
 *
 * This view is used by the CurrentScreenTool to show an overlay with
 * the names of the current activity and fragment.
 *
 * @param context The context used to inflate the view
 */
internal class CurrentScreenView(context: Context) : ConstraintLayout(context) {

    /** View binding for the current screen view layout */
    val binding = PlutoToolCurrentScreenViewBinding.inflate(LayoutInflater.from(context), this, true)

    /** Stores the last activity name to handle cases where the new value is empty */
    private var lastActivityName: CharSequence? = null

    /** Stores the last fragment name to handle cases where the new value is empty */
    private var lastFragmentName: CharSequence? = null

    /**
     * Updates the displayed activity and fragment names.
     *
     * If the activity is from the Pluto package, it shows a special message
     * and hides the fragment name.
     *
     * @param activity The name of the current activity, or null if none
     * @param fragment The name of the current fragment, or null if none
     */
    fun updateText(activity: CharSequence?, fragment: CharSequence?) {
        if ((activity ?: "").startsWith(PLUTO_PKG_PREFIX, true)) {
            updateActivity(
                context.createSpan {
                    append(light(italic(fontColor("~ Pluto Screen ~", context.color(com.pluto.plugin.R.color.pluto___white_60)))))
                }
            )
            updateFragment(null)
        } else {
            updateActivity(activity)
            updateFragment(fragment)
        }
    }

    /**
     * Updates the displayed fragment name.
     *
     * If the fragment name is not null, it shows the fragment group and updates the text.
     * If the fragment name is null, it hides the fragment group.
     *
     * @param fragment The name of the current fragment, or null if none
     */
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

    /**
     * Updates the displayed activity name.
     *
     * If the activity name is not null, it shows the activity group and updates the text.
     * If the activity name is null, it hides the activity group.
     *
     * @param activity The name of the current activity, or null if none
     */
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
        /** Prefix used to identify Pluto's own screens */
        private const val PLUTO_PKG_PREFIX = "com.pluto"
    }
}
