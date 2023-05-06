package com.pluto.plugins.rooms.db.internal.ui.filter.value.components

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

internal abstract class BaseValueStub : ConstraintLayout {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    @Throws(IllegalStateException::class)
    abstract fun getValue(): String?
    abstract fun setValue(value: String?)
}
