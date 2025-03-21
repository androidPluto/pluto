package com.pluto.core.notch

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.pluto.R
import com.pluto.databinding.PlutoLayoutNotchBinding
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.hapticFeedback
import com.pluto.utilities.soundFeedback
import kotlin.math.abs

/**
 * Manages the creation, display, and interaction of the notch view.
 *
 * This class is responsible for creating the notch view, handling touch events,
 * managing its position on screen, and applying the appropriate styling based on
 * the current theme settings.
 *
 * @property context The context used to create and style the view
 * @property listener Listener for notch interaction events
 */
internal class NotchViewManager(
    context: Context,
    private val listener: OnNotchInteractionListener
) {
    /** Device information used to calculate screen dimensions and limits */
    private val device = Device(context)

    /** Upper limit for vertical dragging of the notch */
    private val dragUpLimit = device.screen.heightPx * DRAG_UP_THRESHOLD

    /** Lower limit for vertical dragging of the notch */
    private val dragDownLimit = device.screen.heightPx * DRAG_DOWN_THRESHOLD

    /** The notch view instance */
    var view: View? = null

    /** Layout parameters for positioning the notch on screen */
    val layoutParams = getInitialLayoutParams(context)

    /**
     * Initializes the notch view with touch listeners and styling.
     *
     * Sets up touch handling for click and drag operations, and configures
     * the view's appearance based on the current theme settings.
     *
     * @param context The context used to access resources and settings
     * @param view The notch view to initialize
     */
    private fun initView(context: Context, view: View) {
        /**
         * Touch listener that handles click and drag operations on the notch.
         *
         * Detects:
         * - Click events (ACTION_DOWN followed by ACTION_UP without movement)
         * - Drag events (ACTION_MOVE) to reposition the notch vertically
         */
        view.setOnTouchListener(object : View.OnTouchListener {
            /** Tracks the last motion event action to detect clicks */
            private var lastAction = 0

            /** Initial X position of the notch before dragging */
            private var initialX = 0

            /** Initial Y position of the notch before dragging */
            private var initialY = 0

            /** Initial X touch position when dragging starts */
            private var initialTouchX = 0f

            /** Initial Y touch position when dragging starts */
            private var initialTouchY = 0f

            /**
             * Handles touch events on the notch view.
             *
             * @param v The view being touched
             * @param event The motion event
             * @return True if the event was handled, false otherwise
             */
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // remember the initial position.
                        initialX = layoutParams.x
                        initialY = layoutParams.y
                        // get the touch location
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        lastAction = event.action
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            view.hapticFeedback(true)
                            view.soundFeedback()
                            listener.onClick()
                        }
                        lastAction = event.action
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val movementX = event.rawX - initialTouchX
                        val movementY = event.rawY - initialTouchY

                        if (abs(movementX) > 1 || abs(movementY) > 1) {
//                            layoutParams.x = initialX + movementX.toInt()
                            val currentY = initialY + (event.rawY - initialTouchY).toInt()
                            if (currentY > dragUpLimit && currentY < dragDownLimit) {
                                layoutParams.y = currentY

                                listener.onLayoutParamsUpdated(layoutParams)
                                lastAction = event.action
                                return true
                            }
                            return false
                        }
                        return false
                    }
                }
                return false
            }
        })

        /**
         * Listener that applies styling and positioning when the view is attached to the window.
         * Configures colors based on the current theme settings and positions the notch
         * according to user preferences.
         */
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            /**
             * Called when the view is attached to the window.
             * Applies theme-specific styling and positioning.
             *
             * @param v The attached view
             */
            override fun onViewAttachedToWindow(v: View) {
                PlutoLayoutNotchBinding.bind(v).apply {
                    card.setCardBackgroundColor(
                        context.color(
                            if (SettingsPreferences.isDarkThemeEnabled) {
                                com.pluto.plugin.R.color.pluto___notch_bg_dark
                            } else {
                                com.pluto.plugin.R.color.pluto___notch_bg_light
                            }
                        )
                    )
                    left.setTextColor(
                        context.color(
                            if (SettingsPreferences.isDarkThemeEnabled) {
                                com.pluto.plugin.R.color.pluto___white_80
                            } else {
                                com.pluto.plugin.R.color.pluto___text_dark_80
                            }
                        )
                    )
                    right.setTextColor(
                        context.color(
                            if (SettingsPreferences.isDarkThemeEnabled) {
                                com.pluto.plugin.R.color.pluto___white_80
                            } else {
                                com.pluto.plugin.R.color.pluto___text_dark_80
                            }
                        )
                    )
                    bottom.setBackgroundColor(
                        context.color(
                            if (SettingsPreferences.isDarkThemeEnabled) {
                                com.pluto.plugin.R.color.pluto___notch_accent_dark
                            } else {
                                com.pluto.plugin.R.color.pluto___notch_accent_light
                            }
                        )
                    )
                }
                val gravityHorizontal =
                    if (SettingsPreferences.isRightHandedAccessPopup) Gravity.END else Gravity.START
                layoutParams.gravity = gravityHorizontal or Gravity.TOP
                listener.onLayoutParamsUpdated(layoutParams)
            }

            /** Called when the view is detached from the window. Not used in this implementation. */
            override fun onViewDetachedFromWindow(v: View) {
            }
        })
    }

    /**
     * Creates and configures the initial layout parameters for the notch view.
     *
     * Sets up the window type, flags, gravity, and initial position based on
     * device characteristics and user preferences.
     *
     * @param context The context used to access resources and settings
     * @return The configured WindowManager.LayoutParams
     */
    private fun getInitialLayoutParams(context: Context): WindowManager.LayoutParams {
        val params: WindowManager.LayoutParams
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
            )
        } else {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
            )
        }

        val gravityHorizontal =
            if (SettingsPreferences.isRightHandedAccessPopup) Gravity.END else Gravity.START
        params.gravity = gravityHorizontal or Gravity.TOP
        params.x =
            (context.resources.getDimension(com.pluto.plugin.R.dimen.pluto___popup_bubble_width) * INIT_THRESHOLD_X).toInt()
        params.y = (device.screen.heightPx * INIT_THRESHOLD_Y).toInt()

        return params
    }

    /**
     * Adds the notch view to the window if it doesn't already exist.
     *
     * Creates a new notch view, initializes it, and adds it to the window manager.
     *
     * @param context The context used to create the view
     * @param windowManager The window manager to add the view to
     */
    fun addView(context: Context, windowManager: WindowManager) {
        if (view == null) {
            view = context.inflate(R.layout.pluto___layout_notch)
            view?.let {
                initView(context, it)
                if (it.parent == null) {
                    windowManager.addView(it, layoutParams)
                }
            }
        }
    }

    /**
     * Removes the notch view from the window if it exists.
     *
     * @param windowManager The window manager to remove the view from
     */
    fun removeView(windowManager: WindowManager) {
        view?.parent?.let {
            windowManager.removeView(view)
            view = null
        }
    }

    companion object {
        /** Threshold for the upper limit of vertical dragging (3% of screen height) */
        const val DRAG_UP_THRESHOLD = 0.03

        /** Threshold for the lower limit of vertical dragging (90% of screen height) */
        const val DRAG_DOWN_THRESHOLD = 0.9

        /** Initial horizontal position threshold (-55% of bubble width) */
        const val INIT_THRESHOLD_X = -0.55

        /** Initial vertical position threshold (65% of screen height) */
        const val INIT_THRESHOLD_Y = 0.65
    }
}
