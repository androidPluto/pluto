package com.pluto.core.callback

import com.pluto.utilities.SingleLiveEvent

internal class ResetDataCallback {
    internal val state: SingleLiveEvent<Boolean> = SingleLiveEvent()
}
