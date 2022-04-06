package com.pluto.plugins.utilities.sharing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pluto.plugins.utilities.SingleLiveEvent

class ContentShareViewModel : ViewModel() {

    val data: LiveData<Shareable>
        get() = _data
    private val _data = SingleLiveEvent<Shareable>()

    val action: LiveData<ShareAction>
        get() = _action
    private val _action = SingleLiveEvent<ShareAction>()

    fun performAction(action: ShareAction) {
        _action.postValue(action)
    }

    fun share(shareable: Shareable) {
        _data.postValue(shareable)
    }
}
