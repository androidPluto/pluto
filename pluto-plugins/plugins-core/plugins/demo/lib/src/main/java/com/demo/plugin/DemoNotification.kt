package com.demo.plugin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pluto.plugin.lib_interface.PlutoInterface
import com.pluto.utilities.device.Device

internal class DemoNotification(private val context: Context) {

    private val manager: NotificationManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(NotificationManager::class.java)
        } else {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }
    private val device = Device(context)

    fun add() {
        Session.devIdentifier?.let {
            createChannel()
            val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(context.getString(R.string.demo___notification_title, device.app.name))
                .setContentText(context.getString(R.string.demo___notification_subtitle))
                .setSmallIcon(R.drawable.demo___ic_plugin_icon)
                .setContentIntent(PlutoInterface.notification.getPendingIntent(it))
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setSilent(true)
                .setSound(null)
                .build()
            manager?.notify(NOTIFICATION_ID, notification)
        }
    }

    fun remove() {
        manager?.cancel(NOTIFICATION_ID)
    }

    private fun createNotificationChannel(channel: NotificationChannel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setShowBadge(false)
            createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 10_001
        const val CHANNEL_ID = "pluto_notifications"
        const val GROUP_ID = "pluto_notifications_group"
        const val CHANNEL_NAME = "Pluto Notifications"
    }
}
