package com.pluto.plugins.datastore.pref.internal.compose

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluto.plugins.datastore.pref.R
import com.pluto.plugins.datastore.pref.internal.PrefElement
import com.pluto.plugins.datastore.pref.internal.Type

@Composable
@SuppressWarnings("LongMethod")
internal fun PrefListItem(
    element: PrefElement,
    modifier: Modifier = Modifier,
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null),
    updateValue: (PrefElement, String) -> Unit = { _, _ -> },
    onFocus: () -> Unit = {},
) {
    val isEditing =
        editableItem.value?.name == element.prefName && editableItem.value?.key == element.key
    val newValue = remember {
        mutableStateOf(TextFieldValue(element.value, TextRange(element.value.length)))
    }
    Column(
        modifier = modifier
            .animateContentSize()
            .clickable(enabled = !isEditing) {
                editableItem.value = PreferenceKey(element.prefName, element.key)
                newValue.value = TextFieldValue(
                    element.value,
                    TextRange(element.value.length)
                )
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
                color = colorResource(id = R.color.pluto___text_dark_40),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 12.sp
                )
            )
            Text(
                text = element.type.displayText,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = colorResource(id = R.color.pluto___dull_green_08),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(bottom = 2.dp, start = 8.dp, end = 8.dp),
                color = colorResource(id = R.color.pluto___dull_green),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli_semibold)),
                    fontSize = 10.sp
                )
            )
        }
        Element(
            element = element,
            updateValue = updateValue,
            isEditing = isEditing,
            newValue = newValue,
            editableItem = editableItem,
            onFocus = onFocus
        )
        Divider(Modifier.padding(top = 8.dp), color = colorResource(id = R.color.pluto___dark_05))
    }
}

@Composable
private fun Element(
    element: PrefElement,
    updateValue: (PrefElement, String) -> Unit,
    isEditing: Boolean = false,
    newValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("")),
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null),
    onFocus: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    if (isEditing) {
        EditableField(
            newValue,
            focusRequester,
            onFocus,
            element,
            updateValue,
            editableItem
        )
    } else {
        Text(
            text = element.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.muli))
            )
        )
    }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableField(
    newValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("")),
    focusRequester: FocusRequester,
    onFocus: () -> Unit = {},
    element: PrefElement,
    updateValue: (PrefElement, String) -> Unit,
    editableItem: MutableState<PreferenceKey?> = mutableStateOf(null)
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = newValue.value,
            modifier = Modifier
                .focusTarget()
                .focusRequester(focusRequester)
                .onFocusEvent {
                    if (it.isFocused) {
                        onFocus()
                    }
                }
                .weight(1f),
            onValueChange = { input ->
                newValue.value = input
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(id = R.color.pluto___text_dark_60),
                unfocusedBorderColor = colorResource(id = R.color.pluto___text_dark_20)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = when (element.type) {
                    Type.TypeString, Type.TypeBoolean -> KeyboardType.Text
                    Type.TypeLong, Type.TypeFloat -> KeyboardType.Number
                    else -> KeyboardType.Text
                },
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    updateValue(element, newValue.value.text)
                    editableItem.value = null
                }
            )
        )
        ElementCta(
            onSave = {
                updateValue(element, newValue.value.text)
                editableItem.value = null
            },
            onCancel = {
                editableItem.value = null
                newValue.value = TextFieldValue(
                    element.value,
                    TextRange(element.value.length)
                )
            }
        )
    }
}

@Composable
private fun ElementCta(
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Image(
            modifier = Modifier
                .clickable(onClick = onCancel)
                .size(width = 48.dp, height = 38.dp)
                .padding(horizontal = 12.dp)
                .padding(top = 10.dp, bottom = 4.dp),
            painter = painterResource(id = R.drawable.pluto_dts___ic_clear),
            contentDescription = "cancel"
        )
        Image(
            modifier = Modifier
                .clickable(onClick = onSave)
                .size(width = 48.dp, height = 38.dp)
                .padding(horizontal = 12.dp)
                .padding(top = 4.dp, bottom = 10.dp),
            painter = painterResource(id = R.drawable.pluto_dts___ic_check),
            contentDescription = "save",
            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.pluto___dull_green))
        )
    }
}

@Composable
@Preview("normal item")
private fun PreviewListItem() {
    LazyColumn {
        item {
            PrefListItem(
                element = PrefElement(
                    "Preferences",
                    "key param",
                    "value of the key",
                    Type.TypeString
                ),
                modifier = Modifier.background(colorResource(id = R.color.pluto___white))
            )
        }
    }
}

@Composable
@Preview("very long item")
private fun PreviewLongContentListItem() {
    LazyColumn {
        item {
            PrefListItem(
                element = PrefElement(
                    "Preferences",
                    "VERY VERY VERY VERY VERY very very very very very very Loooong Key",
                    "VERY VERY VERY VERY VERY very very very very Loooong value",
                    Type.TypeBoolean
                ),
                modifier = Modifier.background(colorResource(id = R.color.pluto___white))
            )
        }
    }
}
