package com.pluto.plugin.utilities.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.pluto.plugin.PluginUiBridge
import com.pluto.plugin.utilities.extensions.toast

class PlutoNotification private constructor() {

    companion object {
        fun generatePendingIntent(context: Context, identifier: String, bundle: Bundle? = null): PendingIntent {
            val notificationIntent = Intent(context, PluginUiBridge.get.bridgeComponents.activityClass)
                .putExtra(ID_LABEL, identifier)
            bundle?.let { notificationIntent.putExtra(BUNDLE_LABEL, it) }
            notificationIntent.action = System.currentTimeMillis().toString()
            context.toast("notification : $identifier - ${notificationIntent.getStringExtra(ID_LABEL)}")
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            } else {
                PendingIntent.getActivity(context, 0, notificationIntent, 0)
            }
        }

        const val ID_LABEL = "pluginIdentifier"
        const val BUNDLE_LABEL = "pluginBundle"
    }
}
