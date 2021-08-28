package com.mocklets.pluto.core.sharing

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.mocklets.pluto.core.extensions.toast

internal class ContentShare(activity: FragmentActivity) {
    private val sharer: ContentShareViewModel by activity.lazyContentSharer()
    private val dialog = ShareOptionsDialog(activity) {
        when (it) {
            is ShareAction.ShareAsText -> activity.share(it.shareable.content, it.shareable.title)
            is ShareAction.ShareAsFile -> activity.shareFile(it.shareable.content, it.shareable.title, it.shareable.fileName)
            is ShareAction.ShareAsCopy -> activity.let { context ->
                context.copyToClipboard(it.shareable.content, it.shareable.title)
                context.toast("Content copied to Clipboard")
            }
        }
    }

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

internal sealed class ShareAction {
    data class ShareAsText(val shareable: Shareable) : ShareAction()
    data class ShareAsFile(val shareable: Shareable) : ShareAction()
    data class ShareAsCopy(val shareable: Shareable) : ShareAction()
}
