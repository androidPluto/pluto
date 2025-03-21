package com.pluto.settings

import com.pluto.utilities.SingleLiveEvent

/**
 * Callback class for handling data reset operations in the settings module.
 *
 * This class provides a mechanism to notify observers when a data reset operation
 * is triggered. It uses a SingleLiveEvent to ensure the reset event is only handled once.
 */
internal class ResetDataCallback {
    /**
     * A SingleLiveEvent that represents the state of the reset operation.
     * When set to true, it indicates that a reset operation has been requested.
     */
    val state: SingleLiveEvent<Boolean> = SingleLiveEvent()
}
