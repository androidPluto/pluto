package com.mocklets.pluto.utilities.routing

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

sealed class RouterAction {
    class PopBackStack(val popTag: String? = null, val inclusive: Boolean = false) : RouterAction()
    class BackToApp(val trigger: String) : RouterAction()
}

open class Screens : RouterAction() {

    val tag = this.javaClass.simpleName

    object Settings : Screens()
    object About : Screens()
}

internal sealed class Action {
    class Switch(
        val fragmentTag: String,
        val fragment: Fragment,
        val replace: Boolean = true
    ) : Action()

    class ShowDialog(
        val fragmentTag: String,
        val fragment: DialogFragment,
        val addToBackStack: Boolean = true
    ) : Action()

    class PopStack(
        val tag: String? = null,
        val inclusive: Boolean = false
    ) : Action()
}
