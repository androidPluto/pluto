package com.mocklets.pluto.core.sharing

import androidx.lifecycle.ViewModel
import com.mocklets.pluto.core.SingleLiveEvent

internal class ContentShareViewModel : ViewModel() {

    val data = SingleLiveEvent<Shareable>()

    fun share(shareable: Shareable) {
        data.postValue(shareable)
    }
}
