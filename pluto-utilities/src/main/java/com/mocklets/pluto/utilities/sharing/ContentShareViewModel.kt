package com.mocklets.pluto.utilities.sharing

import androidx.lifecycle.ViewModel
import com.mocklets.pluto.utilities.SingleLiveEvent

class ContentShareViewModel : ViewModel() {

    val data = SingleLiveEvent<Shareable>()

    fun share(shareable: Shareable) {
        data.postValue(shareable)
    }
}
