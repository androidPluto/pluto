package com.pluto.plugins.layoutinspector.internal.inspect

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pluto.utilities.SingleLiveEvent

internal class InspectViewModel(application: Application) : AndroidViewModel(application) {

    val view: LiveData<View>
        get() = _view
    private val _view = SingleLiveEvent<View>()

    fun select(view: View) {
        _view.postValue(view)
    }
}