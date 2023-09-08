package com.pluto.plugins.network.internal

import com.pluto.plugins.network.RequestData

internal interface RequestConverter<T> {
    fun T.convert(): RequestData
}
