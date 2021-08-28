package com.mocklets.pluto.core.sharing

import android.content.Context

internal class ContentShare(private val context: Context) {
    private val dialog = ShareOptionsDialog(context)
    fun share(shareable: Shareable) {
        dialog.show(context, shareable)
    }
}

internal data class Shareable(
    val title: String,
    val content: String,
    val fileName: String = "File shared from Pluto"
)
