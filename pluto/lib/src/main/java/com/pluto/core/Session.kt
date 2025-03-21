package com.pluto.core

/**
 * Maintains the current debugging session state.
 *
 * This class tracks session-level information such as whether consent
 * has been shown to the user.
 */
internal class Session {
    /** Tracks whether the consent dialog has already been shown to the user */
    var isConsentAlreadyShown = false
}
