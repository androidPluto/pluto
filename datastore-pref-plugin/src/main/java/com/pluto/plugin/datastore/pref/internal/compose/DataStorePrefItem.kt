package com.pluto.plugin.datastore.pref.internal.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluto.plugin.datastore.pref.R
import com.pluto.plugin.datastore.pref.internal.PrefElement
import com.pluto.plugin.datastore.pref.internal.PrefUiModel
import com.pluto.plugin.datastore.pref.internal.Type

@Preview
@Composable
private fun DataStorePrefItemPreview() {
    LazyColumn(
        modifier = Modifier
//            .animateItemPlacement()       enable when updating compose to latest
            .wrapContentHeight(Alignment.Top)
            .background(Color(0xFFFFFFFF))
    ) {
        dataStorePrefItems(
            PrefUiModel(
                "Preferences",
                listOf(
                    PrefElement("Preferences", "key", "value", Type.TYPE_STRING),
                    PrefElement("Preferences", "key1", "value1", Type.TYPE_STRING),
                    PrefElement("Preferences", "key2", "value2", Type.TYPE_STRING),
                    PrefElement("Preferences", "key3", "value3", Type.TYPE_STRING),
                    PrefElement(
                        "Preferences",
                        "VERY VERY VERY VERY VERY very very very very very very Loooong Key",
                        "VERY VERY VERY VERY VERY very very very very Loooong value",
                        Type.TYPE_STRING
                    ),
                    PrefElement("Preferences", "key5", "value5", Type.TYPE_STRING),
                )
            ),
        )
    }
}

fun LazyListScope.dataStorePrefItems(
    data: PrefUiModel,
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null),
    updateValue: (PrefElement, String) -> Unit = { _, _ -> }
) {
    item(data.name + "title") {
        Column(
            modifier = Modifier.clickable {
                data.isExpanded.value = !data.isExpanded.value
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val degrees = remember { Animatable(1f) }
                Text(
                    text = data.name,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    ),
                    letterSpacing = 1.2.sp,
                    fontSize = 16.sp
                )

                LaunchedEffect(data.isExpanded.value) {
                    degrees.animateTo(if (data.isExpanded.value) 180f else 0f)
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_expand_more_24),
                    contentDescription = "expand",
                    Modifier
                        .rotate(degrees.value)
                )
            }
            Divider(Modifier.padding(top = 4.dp), color = Color(0xFFF3F3F3))
        }
    }
    data.data.forEach { element ->
        item(element.key) {
            AnimatedVisibility(
                visible = data.isExpanded.value,
                enter = expandVertically(expandFrom = Alignment.CenterVertically),
                exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically) // change with animateItemPlacement() when updating compose to latest
            ) {
                PrefListItem(
                    element = element,
                    editableItem = editableItem,
                    updateValue = updateValue
                )
            }
        }
    }
}
