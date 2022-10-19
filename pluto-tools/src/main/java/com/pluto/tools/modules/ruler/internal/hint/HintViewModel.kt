package com.pluto.tools.modules.ruler.internal.hint

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

internal class HintViewModel(application: Application) : AndroidViewModel(application) {

    val list: LiveData<List<HintItem>>
        get() = _list
    private val _list = MutableLiveData<List<HintItem>>()

    init {
        generate(getApplication())
    }

    private fun generate(context: Context?) {
        context?.apply {
            val list = arrayListOf<HintItem>()
            list.add(HintItem("hello"))
            list.add(HintItem("bye bye"))
            _list.postValue(list)
        }
    }
}
