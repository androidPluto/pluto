package com.pluto.settings

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.list.ListItem

/**
 * ViewModel for the Settings screen that manages the list of setting options and handles reset operations.
 *
 * This ViewModel is responsible for generating the list of settings items to be displayed
 * in the settings UI and handling the reset all settings operation.
 *
 * @param application The application instance used to access application context
 */
internal class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * LiveData containing the list of setting items to be displayed in the UI.
     * This is exposed as a read-only LiveData to prevent modification from outside.
     */
    val list: LiveData<List<ListItem>>
        get() = _list
    private val _list = MutableLiveData<List<ListItem>>()

    /**
     * SingleLiveEvent that signals when all settings should be reset.
     * Using SingleLiveEvent ensures the reset event is only handled once.
     */
    val resetAll = SingleLiveEvent<Boolean>()

    init {
        generate(getApplication())
    }

    /**
     * Generates the list of settings items based on device capabilities and requirements.
     *
     * @param context The context used to access resources and system information
     */
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

    /**
     * Triggers a reset of all settings by posting a true value to the resetAll SingleLiveEvent.
     * This will notify all observers that a reset operation has been requested.
     */
    fun resetAll() {
        resetAll.postValue(true)
    }
}
