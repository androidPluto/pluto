package com.pluto.plugins.datastore.pref.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pluto.plugins.datastore.pref.internal.compose.DataStorePrefComposable
import com.pluto.plugins.datastore.pref.internal.compose.FilterView
import kotlin.math.max
import kotlinx.coroutines.flow.update

internal class DatastorePreferencePluginFragment : Fragment() {

    private val viewModel by viewModels<DatastorePreferencePluginViewModel>()
    private val insets = mutableStateOf(Insets.NONE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(context = requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    val state = viewModel.output.collectAsState(initial = null)
                    state.value?.let {
                        DataStorePrefComposable(
                            data = it,
                            insets = insets,
                            onExit = { activity?.finish() },
                            onFilterClick = { viewModel.showFilterView.update { true } },
                            updateValue = viewModel.updateValue
                        )
                    }
                    FilterView(
                        showFilterState = viewModel.showFilterView,
                        filterState = viewModel.filteredPref,
                        insets = insets,
                    )
                }
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
