package com.pluto.tool.modules.currentScreen

/**
 * Interface for receiving updates about the current screen.
 *
 * This interface is used to notify listeners when the current activity
 * or fragment changes. Implementations can use this information to
 * display or log the current screen information.
 */
internal interface OnCurrentScreenUpdateListener {
    /**
     * Called when the current screen changes.
     *
     * @param fragment The name of the current fragment, or null if none
     * @param activity The name of the current activity, or null if none
     */
    fun onUpdate(fragment: String?, activity: String?)
}
