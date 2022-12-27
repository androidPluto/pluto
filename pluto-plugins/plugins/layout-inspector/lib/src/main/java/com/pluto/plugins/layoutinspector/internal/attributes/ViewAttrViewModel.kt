package com.pluto.plugins.layoutinspector.internal.attributes

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.layoutinspector.internal.ViewUtils.getIdString
import com.pluto.plugins.layoutinspector.internal.attributes.list.AttributeTitle
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttrParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.utilities.list.ListItem
import kotlinx.coroutines.launch

internal class ViewAttrViewModel(application: Application) : AndroidViewModel(application) {

    private val parser = AttrParser()

    val list: LiveData<ArrayList<ListItem>>
        get() = _list
    private val _list = MutableLiveData<ArrayList<ListItem>>()

    fun parse(v: View) {
        viewModelScope.launch {
            val attrList = arrayListOf<ListItem>()
            v.getIdString()?.let {
                attrList.add(Attribute("id", it))
            }
            attrList.add(Attribute("view_type", if (v is ViewGroup) "viewGroup" else "view"))
            attrList.add(Attribute("view_class", v.javaClass.canonicalName))
            val parsedList = parser.parse(v)
            if (parsedList.isNotEmpty()) {
                var category: String? = null
                for (attr in parsedList) {
                    if (category != attr.category) {
                        category = attr.category
                        attrList.add(AttributeTitle(attr.category))
                    }
                    attrList.add(attr)
                }
            }
            _list.postValue(attrList)
        }
    }
}
