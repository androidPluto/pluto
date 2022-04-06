package com.pluto.plugins.datastore.pref.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pluto.plugins.datastore.pref.R
import com.pluto.plugins.datastore.pref.internal.PrefElement
import com.pluto.plugins.datastore.pref.internal.PrefUiModel

@Composable
internal fun DataStorePrefComposable(
    data: List<PrefUiModel>,
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    updateValue: (PrefElement, String) -> Unit = { _, _ -> }
) {
    val editableItem = remember {
        mutableStateOf<PreferenceKey?>(null)
    }
    Column {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            contentDescription = "go back",
            modifier = Modifier
                .clickable { onExit() }
                .padding(all = 16.dp)
        )
        LazyColumn(
            modifier = modifier
                .wrapContentHeight(Alignment.Top)
        ) {
            data.forEach {
                dataStorePrefItems(it, editableItem, updateValue)
            }
        }
    }
}

internal data class PreferenceKey(
    val name: String,
    val key: String,
)
