package com.mocklets.pluto.core.ui.routing

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.mocklets.pluto.modules.exceptions.ui.CrashDetailsFragment
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsFragment
import com.mocklets.pluto.modules.network.ui.details.NetworkCallDetailsFragment

internal sealed class RouterAction {
    class PopBackStack(val popTag: String? = null, val inclusive: Boolean = false) : RouterAction()
    class BackToApp(val trigger: String) : RouterAction()
}

internal sealed class Screens : RouterAction() {

    val tag = this.javaClass.simpleName

    object Settings : Screens()
    object About : Screens()
    class NetworkCallDetails(val data: NetworkCallDetailsFragment.Data) : Screens()
    object NetworkProxySettingsList : Screens()
    class NetworkProxySettings(val data: NetworkProxySettingsFragment.Data? = null) : Screens()
    class CrashDetails(val data: CrashDetailsFragment.Data) : Screens()
    object SharedPrefFilter : Screens()
    object AppState : Screens()
    object CustomActions : Screens()
}

internal sealed class Action {
    class Switch(
        val fragmentTag: String,
        val fragment: Fragment,
        val replace: Boolean = true
    ) : Action()

    class ShowDialog(
        val fragmentTag: String,
        val fragment: DialogFragment,
        val addToBackStack: Boolean = true
    ) : Action()

    class PopStack(
        val tag: String? = null,
        val inclusive: Boolean = false
    ) : Action()
}
