package com.pluto.core.applifecycle

import android.app.Activity
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AppLifecycleTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appLifecycle: AppLifecycle
    private lateinit var appStateCallback: AppStateCallback

    @Mock
    private lateinit var stateObserver: Observer<AppStateCallback.State>

    @Mock
    private lateinit var mockActivity: Activity

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        appStateCallback = AppStateCallback()
        appStateCallback.state.observeForever(stateObserver)
        appLifecycle = AppLifecycle(appStateCallback)
    }

    @Test
    fun `when first activity starts, app state should change to Foreground`() {
        // When
        appLifecycle.onActivityStarted(mockActivity)

        // Then
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)
    }

    @Test
    fun `when second activity starts, app state should remain Foreground without additional updates`() {
        // Given
        appLifecycle.onActivityStarted(mockActivity) // First activity starts
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)

        // When
        val secondActivity = mock(Activity::class.java)
        appLifecycle.onActivityStarted(secondActivity) // Second activity starts

        // Then - verify observer was only called once with Foreground (from the first activity)
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)
    }

    @Test
    fun `when one activity stops but another is still running, app state should remain Foreground`() {
        // Given
        val secondActivity = mock(Activity::class.java)
        appLifecycle.onActivityStarted(mockActivity) // First activity starts
        appLifecycle.onActivityStarted(secondActivity) // Second activity starts

        // When
        appLifecycle.onActivityStopped(mockActivity) // First activity stops

        // Then
        verify(stateObserver, never()).onChanged(AppStateCallback.State.Background)
    }

    @Test
    fun `when all activities stop, app state should change to Background`() {
        // Given
        appLifecycle.onActivityStarted(mockActivity) // Activity starts

        // When
        appLifecycle.onActivityStopped(mockActivity) // Activity stops

        // Then
        verify(stateObserver).onChanged(AppStateCallback.State.Background)
    }

    @Test
    fun `when app goes to background and then foreground, both state changes should be observed`() {
        // Given - app starts in foreground
        appLifecycle.onActivityStarted(mockActivity)
        verify(stateObserver).onChanged(AppStateCallback.State.Foreground)

        // When - app goes to background
        appLifecycle.onActivityStopped(mockActivity)
        verify(stateObserver).onChanged(AppStateCallback.State.Background)

        // When - app comes back to foreground
        appLifecycle.onActivityStarted(mockActivity)

        // Then - verify Foreground state was observed again
        verify(stateObserver, times(2)).onChanged(AppStateCallback.State.Foreground)
    }

    @Test
    fun `other lifecycle methods should not affect app state`() {
        // Given
        val bundle = mock(Bundle::class.java)

        // When
        appLifecycle.onActivityCreated(mockActivity, bundle)
        appLifecycle.onActivityResumed(mockActivity)
        appLifecycle.onActivityPaused(mockActivity)
        appLifecycle.onActivitySaveInstanceState(mockActivity, bundle)
        appLifecycle.onActivityDestroyed(mockActivity)

        // Then
        verify(stateObserver, never()).onChanged(AppStateCallback.State.Foreground)
        verify(stateObserver, never()).onChanged(AppStateCallback.State.Background)
    }
}
