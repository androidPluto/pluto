package com.pluto.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pluto.R
import com.pluto.ui.selector.SelectorActivity
import com.pluto.utilities.device.Device

internal class DebugNotification(private val context: Context) {

    private val manager: NotificationManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(NotificationManager::class.java)
        } else {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }

    private val device = Device(context)

    fun add() {
        val notificationIntent = Intent(context, SelectorActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, notificationIntent, 0)
        }
        createChannel()
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle(context.getString(R.string.pluto___notification_title, device.app.name))
            .setContentText(context.getString(R.string.pluto___notification_subtitle))
            .setSmallIcon(R.drawable.pluto___ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(false)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setSilent(true)
            .setSound(null)
            .build()
        manager?.notify(NOTIFICATION_ID, notification)
    }

    fun remove() {
        manager?.cancel(NOTIFICATION_ID)
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

    private fun createNotificationChannel(channel: NotificationChannel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1011
        const val CHANNEL_ID = "pluto_notifications"
        const val GROUP_ID = "pluto_notifications_group"
        const val CHANNEL_NAME = "Pluto Notifications"
    }
}
