package com.pluto.plugins.datastore.pref.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.pluto.plugins.datastore.pref.R
import com.pluto.plugins.datastore.pref.internal.PrefElement
import com.pluto.plugins.datastore.pref.internal.PrefUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun DataStorePrefComposable(
    data: List<PrefUiModel>,
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    listContentPadding: PaddingValues = PaddingValues(0.dp),
    updateValue: (PrefElement, String) -> Unit = { _, _ -> },
) {
    val editableItem = remember {
        mutableStateOf<PreferenceKey?>(null)
    }
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Column(modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            contentDescription = "go back",
            modifier = Modifier
                .clickable { onExit() }
                .padding(all = 16.dp)
        )
        val density = LocalDensity.current
        LazyColumn(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top),
            state = scrollState,
            contentPadding = listContentPadding
        ) {
            populateList(data, editableItem, updateValue, scope, scrollState, density)
        }
    }
}

private fun LazyListScope.populateList(
    data: List<PrefUiModel>,
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null),
    updateValue: (PrefElement, String) -> Unit = { _, _ -> },
    scope: CoroutineScope,
    scrollState: LazyListState,
    density: Density
) {
    data.forEach { uiModel ->
        dataStorePrefItems(
            uiModel, editableItem, updateValue,
            onFocus = { key ->
                scope.launch {
                    delay(FocusDelay)
                    val itemInfo =
                        scrollState.layoutInfo.visibleItemsInfo.firstOrNull { itemInfo ->
                            itemInfo.key == key
                        }
                    if (itemInfo != null) {
                        scrollState.animateScrollToItem(
                            itemInfo.index,
                            -with(density) {
                                100.dp.roundToPx()
                            }
                        )
                    }
                }
            }
        )
    }
}

const val FocusDelay = 100L

internal data class PreferenceKey(
    val name: String,
    val key: String,
)
