package com.pluto.core.notch

import android.view.WindowManager

/**
 * Interface for handling user interactions with the notch UI component.
 *
 * This interface defines callbacks for click events and layout parameter updates
 * that occur when the user interacts with the notch.
 */
internal interface OnNotchInteractionListener {
    /**
     * Called when the notch is clicked.
     *
     * Implementations should handle the click event, typically by opening
     * the Pluto debugging interface.
     */
    fun onClick()

    /**
     * Called when the notch's layout parameters are updated.
     *
     * This happens when the notch is moved to a new position on the screen.
     * Implementations should update the notch's position in the window.
     *
     * @param params The updated window layout parameters
     */
    fun onLayoutParamsUpdated(params: WindowManager.LayoutParams)
}
