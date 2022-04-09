package com.pluto.plugins.datastore.pref.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluto.plugins.datastore.pref.R

@Composable
@Preview
private fun PreviewToolbar() {
    ToolBar(Modifier.background(CommonColors.background), {}, {})
}

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            contentDescription = "go back",
            modifier = Modifier
                .clickable { onExit() }
                .padding(all = 16.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
            contentDescription = "filter",
            modifier = Modifier
                .clickable { onFilterClick() }
                .padding(all = 16.dp)
        )
    }
}
