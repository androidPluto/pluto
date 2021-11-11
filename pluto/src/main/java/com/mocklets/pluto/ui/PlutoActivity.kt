package com.mocklets.pluto.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.canDrawOverlays
import com.mocklets.pluto.core.extensions.openOverlaySettings
import com.mocklets.pluto.core.preferences.Preferences
import com.mocklets.pluto.core.sharing.ContentShare
import com.mocklets.pluto.core.ui.routing.RouteManager
import com.mocklets.pluto.modules.network.proxy.NetworkProxyViewModel
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsFragment.Companion.IN_APP_BROWSER_RESULT_CODE
import com.mocklets.pluto.modules.setup.easyaccess.EasyAccessSetupDialog

@Keep
internal class PlutoActivity : FragmentActivity(R.layout.pluto___activity_pluto) {

    private lateinit var routeManager: RouteManager
    private lateinit var contentShareHelper: ContentShare
    private val networkProxyViewModel: NetworkProxyViewModel by viewModels()
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        routeManager = RouteManager(this, R.id.container)
        contentShareHelper = ContentShare(this)
        preferences = Preferences(this)

        findViewById<DrawerLayout>(R.id.drawer_layout).setStatusBarBackgroundColor(
            ContextCompat.getColor(this, R.color.pluto___transparent)
        )
    }

    override fun onResume() {
        super.onResume()
        if (!canDrawOverlays() && !preferences.isEasyAccessSetupDialogShown) {
            showEasyAccessSetup()
        }
    }

    override fun onBackPressed() {
        if (!routeManager.onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun showEasyAccessSetup() {
        EasyAccessSetupDialog(this) { openOverlaySettings() }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IN_APP_BROWSER_RESULT_CODE) {
            Pluto.activity.customTabClosed()
            networkProxyViewModel.onInAppBrowserClose()
        }
    }
}
