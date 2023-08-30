package com.pluto.plugins.network.internal.interceptor.logic.core

import io.ktor.http.Url

fun String.asUrl() = Url(this)
