package com.pluto.tool.modules.ruler.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment that hosts the ruler scale view.
 *
 * This fragment is responsible for creating and displaying the RulerScaleView,
 * which provides the actual ruler functionality for measuring UI elements.
 * It's a simple container fragment that creates the view with a unique ID.
 */
internal class RulerFragment : Fragment() {

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * In this case, it creates a new RulerScaleView with a generated ID.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views
     * @param container The parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The created RulerScaleView
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return RulerScaleView(requireContext()).apply {
            id = View.generateViewId()
        }
    }
}
