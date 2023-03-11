package com.pluto.utilities.views.keyvalue.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.views.keyvalue.KeyValuePairEditRequest
import com.pluto.utilities.views.keyvalue.KeyValuePairEditResult

class KeyValuePairEditViewModel : ViewModel() {

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
