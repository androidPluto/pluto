package com.pluto.logger.internal.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pluto.logger.R
import com.pluto.plugin.utilities.notification.PlutoNotification

internal class LoggerNotification(private val context: Context) {

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

    fun add() {
        createChannel()
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle("dummy notification")
            .setContentText("dummy notification text")
            .setSmallIcon(R.drawable.pluto_logger___ic_logger_icon)
            .setContentIntent(PlutoNotification("pluto_logger").generatePendingIntent(context))
            .setOngoing(false)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setSilent(true)
            .setSound(null)
            .build()
        manager?.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 2000
        const val CHANNEL_ID = "dummy_notifications"
        const val GROUP_ID = "dummy_notifications_group"
        const val CHANNEL_NAME = "Dummy Notifications"
    }
}
