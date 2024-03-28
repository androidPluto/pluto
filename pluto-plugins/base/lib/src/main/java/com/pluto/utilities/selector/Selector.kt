package com.pluto.utilities.selector

import android.graphics.drawable.Drawable
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.list.ListItem

class Selector : ViewModel() {

    internal val singleChoiceData: LiveData<SelectorData<SelectorOption>>
        get() = _singleChoiceData
    private val _singleChoiceData = SingleLiveEvent<SelectorData<SelectorOption>>()
    internal val singleChoiceResult = SingleLiveEvent<SelectorOption>()

    internal val multiChoiceData: LiveData<SelectorData<List<SelectorOption>>>
        get() = _multiChoiceData
    private val _multiChoiceData = SingleLiveEvent<SelectorData<List<SelectorOption>>>()
    internal val multiChoiceResult = SingleLiveEvent<List<SelectorOption>>()

    val data = MediatorLiveData<Any>()

    init {
        data.addSource(singleChoiceData) { data.postValue(it) }
        data.addSource(multiChoiceData) { data.postValue(it) }
    }

    fun selectSingle(title: String, list: List<SelectorOption>, preSelected: SelectorOption? = null): SingleLiveEvent<SelectorOption> {
        _singleChoiceData.postValue(SelectorData(title, list, preSelected))
        return singleChoiceResult
    }

    fun selectMultiple(title: String, list: List<SelectorOption>, preSelected: List<SelectorOption>? = null): SingleLiveEvent<List<SelectorOption>> {
        _multiChoiceData.postValue(SelectorData(title, list, preSelected))
        return multiChoiceResult
    }
}

abstract class SelectorOption : ListItem() {
    abstract fun displayText(): CharSequence
    open fun icon(): Drawable? = null
}

data class SelectorData<T>(val title: String, val list: List<SelectorOption>, val preSelected: T?)

fun Fragment.lazySelector(): Lazy<Selector> = activityViewModels()

fun ComponentActivity.lazySelector(): Lazy<Selector> = viewModels()
