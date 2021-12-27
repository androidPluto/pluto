package com.mocklets.pluto.plugin.utilities.sharing

import androidx.lifecycle.ViewModel
import com.mocklets.pluto.plugin.utilities.SingleLiveEvent

class ContentShareViewModel : ViewModel() {

    val data = SingleLiveEvent<Shareable>()

    fun share(shareable: Shareable) {
        data.postValue(shareable)
    }
}
