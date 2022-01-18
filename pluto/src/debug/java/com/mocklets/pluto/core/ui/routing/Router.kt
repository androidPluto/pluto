package com.mocklets.pluto.core.ui.routing

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import com.mocklets.pluto.core.SingleLiveEvent

internal class Router(application: Application) : AndroidViewModel(application) {

    private val routeLiveData = SingleLiveEvent<RouterAction>()
    val routerData: SingleLiveEvent<RouterAction>
        get() = routeLiveData

    fun navigate(screen: Screens) {
        perform(screen)
    }

    fun perform(action: RouterAction) {
        routeLiveData.postValue(action)
    }
}

internal fun Fragment.lazyRouter(): Lazy<Router> = activityViewModels()

internal fun ComponentActivity.lazyRouter(): Lazy<Router> = viewModels()
