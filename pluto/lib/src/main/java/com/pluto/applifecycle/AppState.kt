package com.pluto.applifecycle

internal sealed class AppState {
    object Foreground : AppState()
    object Background : AppState()
}

internal sealed class UiState {
    object Open : UiState()
    object Close : UiState()
}
