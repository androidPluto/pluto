package com.pluto.settings

import com.pluto.utilities.SingleLiveEvent

internal class ResetDataCallback {
    val state: SingleLiveEvent<Boolean> = SingleLiveEvent()
}
