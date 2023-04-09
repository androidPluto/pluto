package com.pluto.plugins.datastore.pref.internal.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluto.plugins.datastore.pref.R
import com.pluto.plugins.datastore.pref.internal.PrefElement
import com.pluto.plugins.datastore.pref.internal.PrefUiModel
import com.pluto.plugins.datastore.pref.internal.Type

@OptIn(ExperimentalFoundationApi::class) // for stickyHeader
@SuppressWarnings("LongMethod")
internal fun LazyListScope.dataStorePrefItems(
    data: PrefUiModel,
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null),
    updateValue: (PrefElement, String) -> Unit = { _, _ -> },
    onFocus: (String) -> Unit = {}
) {
    stickyHeader(data.name + "title") {
        Column(
            modifier = Modifier
                .clickable {
                    data.isExpanded.value = !data.isExpanded.value
                }
                .animateItemPlacement()
                .background(colorResource(id = R.color.pluto___section_color))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.name,
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    ),
                    color = colorResource(id = R.color.pluto___text_dark_80),
                    letterSpacing = 1.2.sp,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.muli_semibold)),
                        fontSize = 16.sp
                    )
                )

                val degrees = remember { Animatable(1f) }

                LaunchedEffect(data.isExpanded.value) {
                    degrees.animateTo(if (data.isExpanded.value) FlipDegree else 0f)
                }

                Image(
                    painter = painterResource(id = R.drawable.pluto_dts___ic_expand),
                    contentDescription = "expand",
                    Modifier
                        .graphicsLayer {
                            rotationZ = degrees.value
                        }
                )
            }
            Divider(Modifier.padding(top = 4.dp), color = colorResource(id = R.color.pluto___dark_05))
        }
    }
    if (data.isExpanded.value) {
        data.data.forEach { element ->
            item(data.name + element.key) {
                PrefListItem(
                    element = element,
                    editableItem = editableItem,
                    updateValue = updateValue,
                    onFocus = {
                        onFocus(data.name + element.key)
                    },
                    modifier = Modifier
                        .animateItemPlacement()
                )
            }
        }
    }
}

@Preview
@Composable
private fun DataStorePrefItemPreview() {
    val prefName = "Preferences"
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .background(colorResource(id = R.color.pluto___white))
    ) {
        dataStorePrefItems(
            PrefUiModel(
                prefName,
                listOf(
                    PrefElement(prefName, "key", "value", Type.TypeString),
                    PrefElement(prefName, "key1", "value1", Type.TypeString),
                    PrefElement(prefName, "key2", "value2", Type.TypeString),
                    PrefElement(prefName, "key3", "value3", Type.TypeString),
                    PrefElement(
                        prefName,
                        "VERY VERY VERY VERY VERY very very very very very very Loooong Key",
                        "VERY VERY VERY VERY VERY very very very very Loooong value",
                        Type.TypeString
                    ),
                    PrefElement(prefName, "key5", "value5", Type.TypeString),
                )
            ),
        )
    }
}

private const val FlipDegree = 180f
