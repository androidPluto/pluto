package com.pluto.utilities.extensions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<List<T>>.add(item: T) {
    val updatedItems = arrayListOf<T>()
    this.value?.let {
        updatedItems.addAll(it)
    }
    updatedItems.add(0, item)
    this.postValue(updatedItems)
}
