package com.pluto.plugins.datastore.pref.internal.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.Insets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

const val ColumnWidthPercentage = .7f

@Composable
internal fun FilterView(
    showFilterState: MutableStateFlow<Boolean>,
    filterState: MutableStateFlow<Map<String, Boolean>>,
    insets: MutableState<Insets>
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = showFilterState.collectAsState().value
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        FilterBackground(visibleState, showFilterState)
        FilterItem(
            visibleState, filterState,
            Modifier
                .padding(
                    top = with(LocalDensity.current) {
                        insets.value.top.toDp()
                    } + 24.dp,
                    end = 24.dp
                )
                .align(Alignment.TopEnd)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterItem(
    visibleState: MutableTransitionState<Boolean>,
    filterState: MutableStateFlow<Map<String, Boolean>>,
    modifier: Modifier,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = expandIn(expandFrom = Alignment.TopEnd),
        exit = shrinkOut(shrinkTowards = Alignment.TopEnd),
        modifier = modifier
            .wrapContentSize()
    ) {
        Column(
            Modifier
                .borderBackground(
                    bgColor = CommonColors.background,
                    borderColor = CommonColors.dividerColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 12.dp)
        ) {
            filterState.collectAsState().value.entries.forEachIndexed { index, entry ->
                Column(
                    Modifier
                        .clickable {
                            filterState.update { srcMap ->
                                mutableMapOf<String, Boolean>().also {
                                    it.putAll(srcMap)
                                    it[entry.key] = !(it[entry.key] ?: true)
                                }
                            }
                        }
                        .fillMaxWidth(ColumnWidthPercentage)
                ) {
                    if (index != 0) {
                        Divider(color = CommonColors.dividerColor)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Checkbox(
                            checked = entry.value,
                            onCheckedChange = { isChecked ->
                                filterState.update { srcMap ->
                                    mutableMapOf<String, Boolean>().also {
                                        it.putAll(srcMap)
                                        it[entry.key] = isChecked
                                    }
                                }
                            },
                            colors = CheckboxDefaults.colors(checkedColor = CommonColors.checkBoxColor)
                        )
                        Text(text = entry.key)
                    }
                }
            }
        }
    }
}

private fun Modifier.borderBackground(
    bgColor: Color,
    borderColor: Color,
    shape: Shape = RectangleShape,
): Modifier {
    return background(
        color = bgColor,
        shape = shape
    ).border(
        width = 1.dp,
        color = borderColor,
        shape = shape
    )
}

@Composable
private fun FilterBackground(
    visibleState: MutableTransitionState<Boolean>,
    showFilterState: MutableStateFlow<Boolean>
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(CommonColors.dialogBackground)
                .clickable {
                    showFilterState.update {
                        false
                    }
                }
        )
    }
}
