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
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttributeParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.plugins.layoutinspector.internal.attributes.type.AttributeType
import com.pluto.utilities.list.ListItem
import kotlinx.coroutines.launch

internal class ViewAttrViewModel(application: Application) : AndroidViewModel(application) {

    private val parser = AttributeParser()

    val list: LiveData<ArrayList<ListItem>>
        get() = _list
    private val _list = MutableLiveData<ArrayList<ListItem>>()

    val shareableAttr: LiveData<String>
        get() = _shareableAttr
    private val _shareableAttr = MutableLiveData<String>()

    fun parse(v: View) {
        viewModelScope.launch {
            val attrList = generateAttributes(v)
            _list.postValue(attrList)

            val shareableAttr = generateAttributeShareable(attrList)
            _shareableAttr.postValue(shareableAttr)
        }
    }

    private fun generateAttributeShareable(attrList: ArrayList<ListItem>): String {
        val text = StringBuilder()
        text.append("View Attributes")
        for (attr in attrList) {
            when (attr) {
                is AttributeTitle -> text.append("\n\n*** attributes from: ${attr.title}")
                is Attribute<*> -> text.append("\n\t${attr.type.title}: ${attr.value}")
            }
        }
        return text.toString()
    }

    private fun generateAttributes(v: View): ArrayList<ListItem> {
        val attrList = arrayListOf<ListItem>()
        v.getIdString()?.let {
            attrList.add(Attribute(AttributeType("id"), it))
        }
        attrList.add(Attribute(AttributeType("view_type"), if (v is ViewGroup) "viewGroup" else "view"))
        attrList.add(Attribute(AttributeType("view_class"), v.javaClass.canonicalName))
        val parsedList = parser.parse(v)
        if (parsedList.isNotEmpty()) {
            for (attr in parsedList) {
                attrList.add(AttributeTitle(attr.parameterizedType))
                attrList.addAll(attr.attributes)
            }
        }
        return attrList
    }
}
