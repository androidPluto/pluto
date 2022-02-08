package com.pluto.plugin.utilities.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

@ColorInt
fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.font(@FontRes id: Int) = ResourcesCompat.getFont(this, id)

fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.toast(message: String, isLong: Boolean = false) {
    Toast.makeText(this, message, if (isLong) LENGTH_LONG else LENGTH_SHORT).show()
}

fun Context.checkAndOpenSupportedApp(uri: Uri?) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast("No app to perform this action")
    }
}
