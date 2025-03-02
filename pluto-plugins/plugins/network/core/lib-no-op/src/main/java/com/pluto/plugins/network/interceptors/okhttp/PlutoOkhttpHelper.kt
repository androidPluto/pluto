package com.pluto.plugins.network.interceptors.okhttp

import okhttp3.OkHttpClient

fun OkHttpClient.Builder.addPlutoOkhttpInterceptor(): OkHttpClient.Builder {
    return this
}
