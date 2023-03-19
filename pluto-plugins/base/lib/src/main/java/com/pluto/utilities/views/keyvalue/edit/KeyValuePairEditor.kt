package com.pluto.utilities.views.keyvalue.edit

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest
import com.pluto.utilities.views.keyvalue.KeyValuePairEditResult

class KeyValuePairEditor : ViewModel() {

    val data: LiveData<KeyValuePairEditRequest>
        get() = _data
    private val _data = SingleLiveEvent<KeyValuePairEditRequest>()

    val result: LiveData<KeyValuePairEditResult>
        get() = _result
    private val _result = SingleLiveEvent<KeyValuePairEditResult>()

    fun edit(data: KeyValuePairEditRequest) {
        _data.postValue(data)
    }

    fun saveResult(result: KeyValuePairEditResult) {
        _result.postValue(result)
    }
}

fun Fragment.lazyKeyValuePairEditor(): Lazy<KeyValuePairEditor> = activityViewModels()

fun ComponentActivity.lazyKeyValuePairEditor(): Lazy<KeyValuePairEditor> = viewModels()
