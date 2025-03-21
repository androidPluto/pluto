package com.pluto.core.applifecycle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AppStateCallbackTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appStateCallback: AppStateCallback

    @Mock
    private lateinit var stateObserver: Observer<AppStateCallback.State>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        appStateCallback = AppStateCallback()
        appStateCallback.state.observeForever(stateObserver)
    }

    @Test
    fun `when state is set to Foreground, observer should receive Foreground state`() {
        // When
        appStateCallback.state.postValue(AppStateCallback.State.Foreground)

        // Then
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)
    }

    @Test
    fun `when state is set to Background, observer should receive Background state`() {
        // When
        appStateCallback.state.postValue(AppStateCallback.State.Background)

        // Then
        verify(stateObserver).onChanged(AppStateCallback.State.Background)
    }

    @Test
    fun `when state changes from Foreground to Background, observer should receive both states in order`() {
        // When
        appStateCallback.state.postValue(AppStateCallback.State.Foreground)
        appStateCallback.state.postValue(AppStateCallback.State.Background)

        // Then
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)
        verify(stateObserver).onChanged(AppStateCallback.State.Background)
    }

    @Test
    fun `when state changes from Background to Foreground, observer should receive both states in order`() {
        // When
        appStateCallback.state.postValue(AppStateCallback.State.Background)
        appStateCallback.state.postValue(AppStateCallback.State.Foreground)

        // Then
        verify(stateObserver).onChanged(AppStateCallback.State.Background)
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)
    }
}
