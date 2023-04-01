package com.pluto.plugins.layoutinspector.internal.hint

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.layoutinspector.R

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
            list.add(HintItem(getString(R.string.pluto_li___hint_select_view)))
            list.add(HintItem(getString(R.string.pluto_li___hint_move_view)))
            _list.postValue(list)
        }
    }
}
