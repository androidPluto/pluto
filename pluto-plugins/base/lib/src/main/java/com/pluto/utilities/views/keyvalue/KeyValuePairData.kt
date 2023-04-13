package com.pluto.utilities.views.keyvalue

import android.text.InputType
import androidx.annotation.Keep
import com.pluto.utilities.list.ListItem

@Keep
data class KeyValuePairData(
    val key: String,
    val value: CharSequence?,
    val iconStart: Int? = null,
    val showClickIndicator: Boolean = false,
    val onClick: (() -> Unit)? = null
)

@Keep
data class KeyValuePairEditRequest(
    val key: String,
    val value: String? = null,
    val hint: String = "enter value",
    private val candidateOptions: List<String>? = null,
    val inputType: KeyValuePairEditInputType = KeyValuePairEditInputType.String,
    val metaData: KeyValuePairEditMetaData
) : ListItem() {
    val shouldAllowFreeEdit: Boolean = inputType != KeyValuePairEditInputType.Selection && inputType != KeyValuePairEditInputType.Boolean
    val processedCandidateOptions: List<String>? = if (inputType == KeyValuePairEditInputType.Boolean) listOf("true", "false") else candidateOptions
    fun isValidValue(text: String?) = when (inputType) {
        is KeyValuePairEditInputType.Integer, is KeyValuePairEditInputType.Float -> !text.isNullOrEmpty()
        else -> true
    }
}

@Keep
data class KeyValuePairEditResult(
    val key: String,
    val value: String?,
    val metaData: KeyValuePairEditMetaData
)

interface KeyValuePairEditMetaData

@Keep
sealed class KeyValuePairEditInputType(val type: Int? = null) {
    object Integer : KeyValuePairEditInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED)

    object Float : KeyValuePairEditInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL)

    object Selection : KeyValuePairEditInputType()

    object Boolean : KeyValuePairEditInputType()

    object String : KeyValuePairEditInputType(InputType.TYPE_CLASS_TEXT)
}
