package com.mocklets.pluto.modules.setup

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.mocklets.pluto.R
import com.mocklets.pluto.core.notification.NotificationUtil
import com.mocklets.pluto.ui.PlutoActivity

internal class SetupNotification(private val context: Context) {

    private val notificationUtil = NotificationUtil(context)

    private val clientAppName: String = context.packageManager.getApplicationLabel(
        context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
    ) as String

    fun add() {
        val notificationIntent = Intent(context, PlutoActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, notificationIntent, 0)
        }

        notificationUtil.notify(
            title = context.getString(R.string.pluto___notification_title, clientAppName),
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
