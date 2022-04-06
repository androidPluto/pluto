package com.pluto

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.pluto.notification.NotificationUtil
import com.pluto.plugins.PluginSelectorActivity
import com.pluto.plugins.utilities.device.Device

internal class DebugNotification(private val context: Context) {

    private val notificationUtil = NotificationUtil(context)

    private val device = Device(context)

    fun add() {
        val notificationIntent = Intent(context, PluginSelectorActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, notificationIntent, 0)
        }

        notificationUtil.notify(
            title = context.getString(R.string.pluto___notification_title, device.app.name),
            text = context.getString(R.string.pluto___notification_subtitle),
            intent = pendingIntent,
            isOngoing = false,
            isAutoCancel = false
        )
    }

    fun remove() {
        notificationUtil.cancel()
    }
}
