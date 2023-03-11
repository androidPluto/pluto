package com.pluto.utilities.views.keyvalue

import android.os.Parcelable
import android.text.InputType
import androidx.annotation.Keep
import com.pluto.utilities.list.ListItem
import kotlinx.parcelize.Parcelize

@Keep
data class KeyValuePairData(
    val key: String,
    val value: CharSequence?,
    val iconStart: Int? = null,
    val showClickIndicator: Boolean = false,
    val onClick: (() -> Unit)? = null
)

@Keep
@Parcelize
data class KeyValuePairEditRequest(
    val key: String,
    val value: String?,
    val hint: String?,
    val candidateOptions: List<String>? = null,
    val allowFreeEdit: Boolean = true,
    val inputType: KeyValuePairEditInputType
) : ListItem(), Parcelable

@Keep
data class KeyValuePairEditResult(
    val data: KeyValuePairEditRequest,
    val result: String?
)

@Keep
sealed class KeyValuePairEditInputType(val type: Int) : Parcelable {
    @Parcelize object Integer : KeyValuePairEditInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED)

    @Parcelize object Float : KeyValuePairEditInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL)

    @Parcelize object String : KeyValuePairEditInputType(InputType.TYPE_CLASS_TEXT)
}
