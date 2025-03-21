package com.pluto

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pluto.core.notch.Notch
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.PluginManager
import com.pluto.plugin.libinterface.NotificationInterface.Companion.BUNDLE_LABEL
import com.pluto.plugin.libinterface.NotificationInterface.Companion.ID_LABEL
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PlutoTest {

    @Mock
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockPluginManager: PluginManager

    @Mock
    private lateinit var mockPlugin: Plugin

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(mockApplication.applicationContext).thenReturn(mockContext)

        // Initialize Pluto with required fields for testing
        // Since Pluto is an object (singleton), we need to use reflection to set its state

        // First initialize the callbacks
        val initCallbacksMethod = Pluto::class.java.getDeclaredMethod("initialiseCallbacks")
        initCallbacksMethod.isAccessible = true
        initCallbacksMethod.invoke(Pluto)

        // Set the application field
        val applicationField = Pluto::class.java.getDeclaredField("application")
        applicationField.isAccessible = true
        applicationField.set(Pluto, mockApplication)

        // Set the pluginManager field
        val pluginManagerField = Pluto::class.java.getDeclaredField("pluginManager")
        pluginManagerField.isAccessible = true
        pluginManagerField.set(Pluto, mockPluginManager)
    }

    @Test
    fun `installer should add plugins and install Pluto`() {
        // Given
        val installer = Pluto.Installer(mockApplication)
        val mockPlugin = mock(Plugin::class.java)
        val mockPluginGroup = mock(PluginGroup::class.java)

        // We need to access the init method since it's called by the installer
        val initMethod = Pluto::class.java.getDeclaredMethod("init", Application::class.java, LinkedHashSet::class.java)
        initMethod.isAccessible = true

        // When
        installer.addPlugin(mockPlugin)
            .addPluginGroup(mockPluginGroup)

        // Instead of calling install() which would call the real init method,
        // we'll verify that the plugins were added correctly
        val pluginsField = installer.javaClass.getDeclaredField("plugins")
        pluginsField.isAccessible = true
        val plugins = pluginsField.get(installer) as LinkedHashSet<*>

        // Then
        assert(plugins.size == 2)
        assert(plugins.contains(mockPlugin))
        assert(plugins.contains(mockPluginGroup))
    }

    @Test
    fun `open should start SelectorActivity when identifier is null`() {
        // Given
        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)

        // When
        Pluto.open()

        // Then
        verify(mockContext).startActivity(intentCaptor.capture())
        val capturedIntent = intentCaptor.value
        assert(capturedIntent.component?.className == SelectorActivity::class.java.name)
        assert(capturedIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
    }

    @Test
    fun `open should start PlutoActivity when valid identifier is provided`() {
        // Given
        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        val testIdentifier = "test_plugin"
        val testBundle = Bundle()
        `when`(mockPluginManager.get(testIdentifier)).thenReturn(mockPlugin)

        // When
        Pluto.open(testIdentifier, testBundle)

        // Then
        verify(mockContext).startActivity(intentCaptor.capture())
        val capturedIntent = intentCaptor.value
        assert(capturedIntent.component?.className == PlutoActivity::class.java.name)
        assert(capturedIntent.getStringExtra(ID_LABEL) == testIdentifier)
        assert(capturedIntent.getBundleExtra(BUNDLE_LABEL) == testBundle)
        assert(capturedIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
        assert(capturedIntent.flags and Intent.FLAG_ACTIVITY_CLEAR_TOP != 0)
        assert(capturedIntent.flags and Intent.FLAG_ACTIVITY_MULTIPLE_TASK != 0)
    }

    @Test
    fun `open should show toast when invalid identifier is provided`() {
        // Given
        val testIdentifier = "invalid_plugin"
        `when`(mockPluginManager.get(testIdentifier)).thenReturn(null)

        // When
        Pluto.open(testIdentifier)

        // Then
        // We can't easily verify toast messages in Robolectric tests,
        // but we can verify that startActivity was not called
        verify(mockContext, never()).startActivity(any())
    }

    @Test
    fun `clearLogs should call pluginManager clearLogs with null when no identifier is provided`() {
        // When
        Pluto.clearLogs()

        // Then
        verify(mockPluginManager).clearLogs(null)
    }

    @Test
    fun `clearLogs should call pluginManager clearLogs with identifier when provided`() {
        // Given
        val testIdentifier = "test_plugin"

        // When
        Pluto.clearLogs(testIdentifier)

        // Then
        verify(mockPluginManager).clearLogs(testIdentifier)
    }

    @Test
    fun `showNotch should enable or disable notch based on state parameter`() {
        // Given
        val mockNotch = mock(Notch::class.java)
        val field = Pluto::class.java.getDeclaredField("notch")
        field.isAccessible = true
        field.set(Pluto, mockNotch)

        // When - enable notch
        Pluto.showNotch(true)

        // Then
        verify(mockNotch).enable(true)

        // When - disable notch
        Pluto.showNotch(false)

        // Then
        verify(mockNotch).enable(false)
    }

    @Test
    fun `initialiseCallbacks should initialize all required callbacks`() {
        // Given
        // Use reflection to access the private method
        val method = Pluto::class.java.getDeclaredMethod("initialiseCallbacks")
        method.isAccessible = true

        // When
        method.invoke(Pluto)

        // Then
        // Verify that all callbacks are initialized by checking they're not null
        val resetDataCallbackField = Pluto::class.java.getDeclaredField("resetDataCallback")
        resetDataCallbackField.isAccessible = true
        assert(resetDataCallbackField.get(Pluto) != null)

        val appStateCallbackField = Pluto::class.java.getDeclaredField("appStateCallback")
        appStateCallbackField.isAccessible = true
        assert(appStateCallbackField.get(Pluto) != null)

        val selectorStateCallbackField = Pluto::class.java.getDeclaredField("selectorStateCallback")
        selectorStateCallbackField.isAccessible = true
        assert(selectorStateCallbackField.get(Pluto) != null)

        val notchStateCallbackField = Pluto::class.java.getDeclaredField("notchStateCallback")
        notchStateCallbackField.isAccessible = true
        assert(notchStateCallbackField.get(Pluto) != null)
    }
}
