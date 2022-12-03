package com.pluto.plugins.layoutinspector.internal

internal class CoordinatePair {
    var x = 0f
    var y = 0f
}

internal sealed class OperableViewState {
    object Idle : OperableViewState()
    object Pressing : OperableViewState()  // after tapTimeout and before longPressTimeout
    object Touching : OperableViewState()  // trigger move before dragging
    object Dragging : OperableViewState()  // since long press
}
