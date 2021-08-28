package com.mocklets.pluto.core.sharing

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels

internal class ContentShare(activity: FragmentActivity) {
    private val dialog = ShareOptionsDialog(activity)
    private val sharer: ContentShareViewModel by activity.lazyContentSharer()

    init {
        sharer.data.observe(
            activity,
            {
                dialog.show(it)
            }
        )
    }
}

internal fun Fragment.lazyContentSharer(): Lazy<ContentShareViewModel> = activityViewModels()

internal fun ComponentActivity.lazyContentSharer(): Lazy<ContentShareViewModel> = viewModels()

internal data class Shareable(
    val title: String,
    val content: String,
    val fileName: String = "File shared from Pluto"
)
