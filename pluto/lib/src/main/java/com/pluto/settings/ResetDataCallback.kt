package com.pluto.settings

import com.pluto.utilities.SingleLiveEvent

internal class ResetDataCallback {
    val state: com.pluto.utilities.SingleLiveEvent<Boolean> = com.pluto.utilities.SingleLiveEvent()
}
