package com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.hint

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.ruler.R

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
            list.add(HintItem(getString(R.string.pluto_ruler___hint_reset_position)))
            list.add(HintItem(getString(R.string.pluto_ruler___hint_measure)))
            _list.postValue(list)
        }
    }
}
