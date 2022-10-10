package com.pluto.core.callback

import com.pluto.utilities.SingleLiveEvent

internal class ResetDataCallback {
    val state: SingleLiveEvent<Boolean> = SingleLiveEvent()
}
