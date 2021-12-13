package com.mocklets.pluto

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

internal class NotificationUtil(private val context: Context) {

    private val manager: NotificationManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(NotificationManager::class.java)
        } else {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
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

    fun cancel() {
        manager?.cancel(NOTIFICATION_ID)
    }

    fun notify(
        title: String,
        text: String,
        intent: PendingIntent,
        isOngoing: Boolean = false,
        isAutoCancel: Boolean = false
    ) {
        createChannel()
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.pluto___ic_launcher)
            .setContentIntent(intent)
            .setOngoing(isOngoing)
            .setOnlyAlertOnce(true)
            .setAutoCancel(isAutoCancel)
            .setSilent(true)
            .setSound(null)
            .build()
        manager?.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 1011
        const val CHANNEL_ID = "pluto_notifications"
        const val GROUP_ID = "pluto_notifications_group"
        const val CHANNEL_NAME = "Pluto Notifications"
    }
}
