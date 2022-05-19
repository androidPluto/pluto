package com.pluto.plugins.datastore.pref.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.pluto.plugins.datastore.pref.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.pluto___dark)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ConstraintLayout(
            modifier = modifier.fillMaxWidth()
        ) {
            val (close, title, filer) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.pluto_dts___ic_close),
                contentDescription = "close",
                modifier = Modifier
                    .constrainAs(close) {
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                        start.linkTo(parent.start)
                    }
                    .clickable { onExit() }
                    .padding(horizontal = 12.dp)
            )
            Text(
                stringResource(id = R.string.pluto_dts___plugin_name),
                color = colorResource(id = R.color.pluto___white),
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(close.end)
                    }
                    .padding(vertical = 16.dp),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli_semibold)),
                    fontSize = 16.sp
                )
            )
            Image(
                painter = painterResource(id = R.drawable.pluto_dts___ic_filter),
                contentDescription = "filter",
                modifier = Modifier
                    .constrainAs(filer) {
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable { onFilterClick() }
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
@Preview
private fun PreviewToolbar() {
    ToolBar(Modifier.background(colorResource(id = R.color.pluto___dark)), {}, {})
}
