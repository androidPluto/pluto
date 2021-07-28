package com.mocklets.pluto.modules.settings

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.logging.LogsRepo
import com.mocklets.pluto.modules.network.NetworkCallsRepo
import com.mocklets.pluto.modules.network.proxy.NetworkProxyRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    val list: LiveData<List<ListItem>>
        get() = _list
    private val _list = MutableLiveData<List<ListItem>>()

    fun generate(context: Context?) {
        context?.apply {
            val list = arrayListOf<ListItem>()

            val isOSAboveM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            if (isOSAboveM) {
                list.add(SettingsEasyAccessEntity())
            }
            list.add(SettingsEasyAccessPopupAppearanceEntity("mode"))
            list.add(SettingsEasyAccessPopupAppearanceEntity("handed"))

//            list.add(SettingsLinkMockletsEntity()) todo enable after Mocklets is integrated

            list.add(SettingsSharedPrefEntity())
            list.add(SettingsResetAllEntity())
            _list.postValue(list)
        }
    }

    fun resetAll() {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkCallsRepo.deleteAll()
            LogsRepo.deleteAll()
            NetworkProxyRepo.deleteAll()
        }
    }
}
