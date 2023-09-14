package com.pluto.plugins.network

import com.pluto.plugins.network.internal.PlutoInterceptor
import okhttp3.OkHttpClient
import javax.net.SocketFactory

fun OkHttpClient.Builder.addPlutoOkhttpInterceptor() {
    // todo add okhttp settings block here
    socketFactory(SocketFactory.getDefault())
    addInterceptor(PlutoInterceptor())
}
