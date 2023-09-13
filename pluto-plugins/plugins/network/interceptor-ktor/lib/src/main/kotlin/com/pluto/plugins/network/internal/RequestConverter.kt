package com.pluto.plugins.network.internal

import com.pluto.plugins.network.intercept.NetworkData.Request

internal interface RequestConverter<T> {
    fun T.convert(): Request
}
