package com.mocklets.pluto.utilities.routing

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import com.mocklets.pluto.utilities.SingleLiveEvent

class Router(application: Application) : AndroidViewModel(application) {

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

fun Fragment.lazyRouter(): Lazy<Router> = activityViewModels()

fun ComponentActivity.lazyRouter(): Lazy<Router> = viewModels()
