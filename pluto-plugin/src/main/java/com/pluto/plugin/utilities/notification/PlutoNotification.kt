package com.pluto.plugin.utilities.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.pluto.plugin.PluginUiBridge

class PlutoNotification(private val identifier: String) {

    fun generatePendingIntent(context: Context, bundle: Bundle? = null): PendingIntent {
        val notificationIntent = Intent(context, PluginUiBridge.get.bridgeComponents.activityClass)
            .putExtra("pluginIdentifier", identifier)
        bundle?.let { notificationIntent.putExtra("pluginBundle", it) }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, notificationIntent, 0)
        }
    }
}
