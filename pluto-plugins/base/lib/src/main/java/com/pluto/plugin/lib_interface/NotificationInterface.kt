package com.pluto.plugin.lib_interface

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class NotificationInterface(private val application: Application, private val pluginActivityClass: Class<out FragmentActivity>) {

    fun getPendingIntent(identifier: String, bundle: Bundle? = null): PendingIntent {
        val notificationIntent = Intent(application.applicationContext, pluginActivityClass)
            .putExtra(ID_LABEL, identifier)
        bundle?.let { notificationIntent.putExtra(BUNDLE_LABEL, it) }
        notificationIntent.action = System.currentTimeMillis().toString()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(application.applicationContext, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getActivity(application.applicationContext, 0, notificationIntent, 0)
        }
    }

    companion object {
        const val ID_LABEL = "pluginIdentifier"
        const val BUNDLE_LABEL = "pluginBundle"
    }
}