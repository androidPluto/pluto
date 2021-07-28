package com.mocklets.pluto.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsFragment.Companion.IN_APP_BROWSER_RESULT_CODE

internal const val CHROME_PACKAGE_NAME = "com.android.chrome"

internal fun Activity.customTab(uri: Uri) {
    val params = CustomTabColorSchemeParams.Builder()
        .setNavigationBarColor(color(R.color.pluto___mocklets_title))
        .setToolbarColor(color(R.color.pluto___mocklets_title))
        .setSecondaryToolbarColor(color(R.color.pluto___mocklets_title))
        .build()

    val customIntentBuilder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
    customIntentBuilder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params)
    customIntentBuilder.setShareState(SHARE_STATE_OFF)
    customIntentBuilder.setUrlBarHidingEnabled(false)
    customIntentBuilder.setShowTitle(false)

    val customTabIntent = customIntentBuilder.build()
    customTabIntent.intent.setPackage(CHROME_PACKAGE_NAME)

    try {
        customTabIntent.intent.data = uri
        Pluto.activity.customTabOpened()
        this.startActivityForResult(customTabIntent.intent, IN_APP_BROWSER_RESULT_CODE)
    } catch (e: ActivityNotFoundException) {
        DebugLog.e("chrome-tab", "not able to handle request", e)
        checkAndOpenSupportedApp(uri)
    }
}

internal fun Context.bindCustomTab(uri: Uri?) {
    val serviceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(name: ComponentName, mClient: CustomTabsClient) {
            DebugLog.d("Service", "Connected")
            mClient.warmup(0L)
            val callback = object : CustomTabsCallback() {}
            val session = mClient.newSession(callback)!!
            uri?.let { session.mayLaunchUrl(it, null, null) }
            val builder = CustomTabsIntent.Builder()
            builder.setSession(session)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            DebugLog.d("Service", "Disconnected")
        }
    }
    CustomTabsClient.bindCustomTabsService(this, CHROME_PACKAGE_NAME, serviceConnection)
}
