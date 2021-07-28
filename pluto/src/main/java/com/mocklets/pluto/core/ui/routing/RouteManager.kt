package com.mocklets.pluto.core.ui.routing

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.hideKeyboard
import com.mocklets.pluto.core.extensions.setParcelExtra
import com.mocklets.pluto.modules.appstate.AppStateFragment
import com.mocklets.pluto.modules.exceptions.ui.CrashDetailsFragment
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsFragment
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsListFragment
import com.mocklets.pluto.modules.network.ui.details.NetworkCallDetailsFragment
import com.mocklets.pluto.modules.preferences.ui.filter.SharedPrefFilterFragment
import com.mocklets.pluto.modules.settings.SettingsFragment
import com.mocklets.pluto.ui.AboutFragment

internal class RouteManager(private val activity: FragmentActivity, private val containerId: Int) {

    private val router by activity.lazyRouter()
    private val fragmentManager
        get() = if (!activity.supportFragmentManager.isDestroyed) {
            activity.supportFragmentManager
        } else null
    private val currentFragment
        get() = fragmentManager?.findFragmentById(containerId)

    init {
        router.routerData.observe(
            activity,
            {
                if (!activity.supportFragmentManager.isStateSaved) {
                    handleRoutingData(it)
                } else {
                    router.perform(it)
                }
            }
        )
    }

    private fun handleRoutingData(data: RouterAction) {
        when (data) {
            is Screens -> executeAction(generateAction(data))
            is RouterAction.PopBackStack -> executeAction(Action.PopStack(data.popTag, data.inclusive))
            is RouterAction.BackToApp -> activity.finish()
        }
    }

    private fun executeAction(action: Action) {
        when (action) {
            is Action.Switch -> {
//                action.resultRequestCode?.let {
//                    action.fragment.setTargetFragment(currentFragment, it)
//                }
                fragmentManager?.beginTransaction()?.apply {
                    animate()
                    if (action.replace) {
                        replace(containerId, action.fragment, action.fragmentTag)
                    } else {
                        add(containerId, action.fragment, action.fragmentTag)
                    }
                    addToBackStack(action.fragmentTag)
                }?.commit()
                activity.hideKeyboard()
            }

            is Action.ShowDialog -> {
//                action.resultRequestCode?.let {
//                    action.fragment.setTargetFragment(currentFragment, it)
//                }
                fragmentManager?.beginTransaction()?.apply {
                    if (action.addToBackStack) {
                        addToBackStack(action.fragmentTag)
                    }
                }?.let { action.fragment.show(it, action.fragmentTag) }
            }

            is Action.PopStack -> fragmentManager?.popBackStack(
                action.tag,
                if (action.inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
            )
        }
    }

    private fun generateAction(screen: Screens): Action {
        return when (screen) {
            is Screens.Settings -> Action.Switch(screen.tag, SettingsFragment())
            is Screens.AppState -> Action.Switch(screen.tag, AppStateFragment())
            is Screens.About -> Action.Switch(screen.tag, AboutFragment(), false)
            is Screens.NetworkCallDetails -> Action.Switch(
                screen.tag,
                NetworkCallDetailsFragment().apply {
                    setParcelExtra(screen.data)
                }
            )
            is Screens.NetworkProxySettings -> Action.Switch(
                screen.tag,
                NetworkProxySettingsFragment().apply {
                    setParcelExtra(screen.data)
                }
            )
            is Screens.NetworkProxySettingsList -> Action.Switch(screen.tag, NetworkProxySettingsListFragment())
            is Screens.CrashDetails -> Action.Switch(
                screen.tag,
                CrashDetailsFragment().apply {
                    setParcelExtra(screen.data)
                }
            )
            is Screens.SharedPrefFilter -> Action.Switch(screen.tag, SharedPrefFilterFragment())
        }
    }

    fun onBackPressed(): Boolean {
        var handled =
            if (currentFragment is BackKeyHandler) {
                (currentFragment as BackKeyHandler).onBackPressed()
            } else {
                false
            }

        if (!handled) {
            if (fragmentManager?.backStackEntryCount ?: 0 > 0) {
                handled = true
                fragmentManager?.popBackStack()
            }
        }
        return handled
    }
}

private fun FragmentTransaction.animate() {
    setCustomAnimations(
        R.anim.pluto___fragment_default_enter,
        R.anim.pluto___fragment_default_exit,
        R.anim.pluto___fragment_default_reenter,
        R.anim.pluto___fragment_default_return
    )
}
