package com.pluto.plugin.datastore.pref.internal.compose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluto.plugin.datastore.pref.R
import com.pluto.plugin.datastore.pref.internal.PrefElement
import com.pluto.plugin.datastore.pref.internal.Type

@Composable
@Preview("normal item")
fun PreviewListItem() {
    LazyColumn {
        item {
            PrefListItem(
                element = PrefElement(
                    "Preferences",
                    "key param",
                    "value of the key",
                    Type.TYPE_STRING
                ),
                modifier = Modifier.background(Color(0xFFFFFFFF)),
            )
        }
    }
}

@Composable
@Preview("very long item")
fun PreviewLongContentListItem() {
    LazyColumn {
        item {
            PrefListItem(
                element = PrefElement(
                    "Preferences",
                    "VERY VERY VERY VERY VERY very very very very very very Loooong Key",
                    "VERY VERY VERY VERY VERY very very very very Loooong value",
                    Type.TYPE_BOOLEAN
                ),
                modifier = Modifier.background(Color(0xFFFFFFFF))
            )
        }
    }
}

@Composable
fun PrefListItem(
    element: PrefElement,
    modifier: Modifier = Modifier,
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null),
    updateValue: (PrefElement, String) -> Unit = { _, _ -> }
) {
    val isEditing =
        editableItem.value?.name == element.prefName && editableItem.value?.key == element.key

    val newValue = remember {
        mutableStateOf(element.value)
    }
    val preferenceKey = PreferenceKey(element.prefName, element.key)

    Column(
        modifier = modifier
            .animateContentSize()
            .clickable(enabled = !isEditing) {
                editableItem.value = preferenceKey
                newValue.value = element.value
            }
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = element.key,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                fontSize = 12.sp,
                color = Color(0xFF78768B)
            )
            Text(
                text = element.type.displayText,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = Color(0xFFF1F7EF),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(bottom = 2.dp, start = 8.dp, end = 8.dp),
                color = Color(0xFF71B36B),
                fontSize = 8.sp,
            )
        }
        Crossfade(targetState = isEditing) { isEditing ->
            if (isEditing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newValue.value,
                        modifier = Modifier
                            .weight(1f),
                        onValueChange = { input ->
                            newValue.value = input
                        },
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Image(
                            modifier = Modifier
                                .clickable {
                                    editableItem.value = null
                                    newValue.value = element.value
                                }
                                .size(width = 48.dp, height = 38.dp)
                                .padding(horizontal = 12.dp)
                                .padding(top = 10.dp, bottom = 4.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                            contentDescription = "delete"
                        )
                        Image(
                            modifier = Modifier
                                .clickable {
                                    updateValue(element, newValue.value)
                                    editableItem.value = null
                                }
                                .size(width = 48.dp, height = 38.dp)
                                .padding(horizontal = 12.dp)
                                .padding(top = 4.dp, bottom = 10.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_check_24),
                            contentDescription = "save",
                            colorFilter = ColorFilter.tint(color = Color(0xFF71B36B))
                        )
                    }
                }
            } else {
                Text(
                    text = element.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 24.dp)
                )
            }
        }
        Divider(Modifier.padding(top = 8.dp), color = Color(0xFFF3F3F2))
    }
}
