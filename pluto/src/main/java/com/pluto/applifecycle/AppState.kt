package com.pluto.applifecycle

internal sealed class AppState {
    object Foreground : AppState()
    object Background : AppState()
}
