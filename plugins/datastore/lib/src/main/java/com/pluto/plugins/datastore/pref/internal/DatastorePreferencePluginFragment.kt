package com.pluto.plugins.datastore.pref.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pluto.plugins.datastore.pref.internal.compose.CommonColors
import com.pluto.plugins.datastore.pref.internal.compose.DataStorePrefComposable
import kotlin.math.max

internal class DatastorePreferencePluginFragment : Fragment() {

    private val viewModel by viewModels<DatastorePreferencePluginViewModel>()
    private val insets = mutableStateOf(Insets.NONE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(context = context!!).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            val state = viewModel.output.collectAsState(initial = null)
            state.value?.let {
                DataStorePrefComposable(
                    it,
                    modifier = Modifier
                        .background(CommonColors.background)
                        .padding(
                            top = with(LocalDensity.current) {
                                insets.value.top.toDp()
                            }
                        ),
                    onExit = {
                        activity?.finish()
                    },
                    listContentPadding = PaddingValues(
                        bottom = with(LocalDensity.current) {
                            insets.value.bottom.toDp()
                        }
                    ),
                    updateValue = viewModel.updateValue
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            insets.value = Insets.of(
                0,
                max(systemBarsInsets.top, imeInsets.top),
                0,
                max(systemBarsInsets.bottom, imeInsets.bottom)
            )
            windowInsets
        }
    }
}
