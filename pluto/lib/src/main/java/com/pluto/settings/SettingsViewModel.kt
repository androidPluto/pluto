package com.pluto.settings

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.list.ListItem

internal class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    val list: LiveData<List<ListItem>>
        get() = _list
    private val _list = MutableLiveData<List<ListItem>>()

    val resetAll = SingleLiveEvent<Boolean>()

    init {
        generate(getApplication())
    }

    private fun generate(context: Context?) {
        context?.apply {
            val list = arrayListOf<ListItem>()

            val isOSAboveM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            if (isOSAboveM) {
                list.add(SettingsEasyAccessEntity())
            }
            list.add(SettingsEasyAccessPopupAppearanceEntity("handed"))
            list.add(SettingsThemeEntity())
            list.add(SettingsGridSizeEntity())
            list.add(SettingsResetAllEntity())
            _list.postValue(list)
        }
    }

    fun resetAll() {
        resetAll.postValue(true)
    }
}
