package com.mocklets.pluto.core.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

internal fun Context.share(message: String, title: String? = null, subject: String? = null) {
    val intent = Intent().apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
        putExtra(Intent.EXTRA_TEXT, message)
        action = Intent.ACTION_SEND
    }
    startActivity(Intent.createChooser(intent, title ?: "Share via..."))
}

internal fun Context.copyToClipboard(label: String, data: String) {
    val clipboard: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(label, data)
    clipboard?.setPrimaryClip(clip)
}
