package com.pluto.utilities.views.keyvalue.edit

import android.text.InputType
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

fun Fragment.lazyKeyValuePairEditor(): Lazy<KeyValuePairEditViewModel> = activityViewModels()

fun ComponentActivity.lazyKeyValuePairEditor(): Lazy<KeyValuePairEditViewModel> = viewModels()

sealed class ValueType(keyboardInputType: Int? = null, selectionOptions: List<String>? = null) {
    class Number(selectionOptions: List<String>? = null) : ValueType(
        keyboardInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED,
        selectionOptions = selectionOptions
    )

    class Float(selectionOptions: List<String>? = null) : ValueType(
        keyboardInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL,
        selectionOptions = selectionOptions
    )

    class Text(selectionOptions: List<String>? = null) : ValueType(keyboardInputType = InputType.TYPE_CLASS_TEXT, selectionOptions = selectionOptions)
    object Boolean : ValueType(
        selectionOptions = listOf(
            "true",
            "false"
        )
    )

    class Selection(selectionOptions: List<String>? = null) : ValueType(selectionOptions = selectionOptions)
}
