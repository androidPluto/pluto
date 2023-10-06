package com.pluto.plugins.network.okhttp

import okhttp3.OkHttpClient

fun OkHttpClient.Builder.addPlutoOkhttpInterceptor(): OkHttpClient.Builder {
    return this
}
