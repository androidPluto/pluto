package com.pluto.plugins.network.interceptors.okhttp

import javax.net.SocketFactory
import okhttp3.OkHttpClient

@Deprecated("add PlutoHttpInterceptor directly to OkHttpClient instead")
fun OkHttpClient.Builder.addPlutoOkhttpInterceptor(): OkHttpClient.Builder {
    // todo add okhttp settings block here
    socketFactory(SocketFactory.getDefault())
    addInterceptor(PlutoOkhttpInterceptor)
    return this
}
