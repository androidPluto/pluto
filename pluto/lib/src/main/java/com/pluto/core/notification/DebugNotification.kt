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

/**
 * Manages the debug notification shown in the notification drawer.
 *
 * This class handles creating, showing, and removing the notification that
 * provides quick access to Pluto's debugging interface. It handles compatibility
 * across different Android versions, including notification channels for Android O+.
 *
 * @property context The context used to create and manage notifications
 */
internal class DebugNotification(private val context: Context) {

    /** The system notification manager used to show and hide notifications */
    private val manager: NotificationManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(NotificationManager::class.java)
        } else {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }

    /** Device information used to get app name for the notification */
    private val device = Device(context)

    /**
     * Creates and shows the debug notification.
     *
     * The notification includes the app name and a message indicating that
     * Pluto is active. Clicking the notification opens the Pluto selector activity.
     */
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

    /**
     * Removes the debug notification from the notification drawer.
     */
    fun remove() {
        manager?.cancel(NOTIFICATION_ID)
    }

    /**
     * Creates the notification channel for Android O and above.
     *
     * This is required for notifications to appear on Android O+.
     * For earlier versions, this method has no effect.
     */
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

    /**
     * Creates a notification channel with the system notification manager.
     *
     * @param channel The notification channel to create
     */
    private fun createNotificationChannel(channel: NotificationChannel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        /** Unique ID for the debug notification */
        const val NOTIFICATION_ID = 1011

        /** ID for the notification channel */
        const val CHANNEL_ID = "pluto_notifications"

        /** ID for the notification group */
        const val GROUP_ID = "pluto_notifications_group"

        /** Human-readable name for the notification channel */
        const val CHANNEL_NAME = "Pluto Notifications"
    }
}
